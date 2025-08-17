package com.nhngcmnh.example.identity_service.config;

import com.nhngcmnh.example.identity_service.repository.InvalidTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;

public class CustomJwtDecoder implements JwtDecoder {
    private final String secret;
    private final InvalidTokenRepository invalidTokenRepository;

    public CustomJwtDecoder(String secret, InvalidTokenRepository invalidTokenRepository) {
        this.secret = secret;
        this.invalidTokenRepository = invalidTokenRepository;
    }

    @Override
    public Jwt decode(String token) throws JwtException {
        Claims claims;
        java.util.Map<String, Object> headers;
        try {
            var jws = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(token);
            claims = jws.getBody();
            headers = jws.getHeader();
        } catch (Exception e) {
            throw new JwtException("Token parsing failed: " + e.getMessage(), e);
        }
        // Check expiration
        Date expiryDate = claims.getExpiration();
        if (expiryDate == null || expiryDate.before(new Date())) {
            throw new JwtValidationException("Token expired", Collections.emptyList());
        }
        // Check invalidated token
        String randomId = claims.get("randomId", String.class);
        if (randomId != null && invalidTokenRepository.existsById(randomId)) {
            throw new JwtValidationException("Token has been invalidated (logout or expired)", Collections.emptyList());
        }
        // Build Jwt object with original headers
        return Jwt.withTokenValue(token)
                .headers(h -> h.putAll(headers))
                .claims(c -> c.putAll(claims))
                .issuedAt(claims.getIssuedAt().toInstant())
                .expiresAt(expiryDate.toInstant())
                .build();
    }
}
