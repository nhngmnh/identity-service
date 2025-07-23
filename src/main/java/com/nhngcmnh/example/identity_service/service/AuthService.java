
package com.nhngcmnh.example.identity_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import com.nhngcmnh.example.identity_service.entity.User;
import com.nhngcmnh.example.identity_service.exception.AppException;
import com.nhngcmnh.example.identity_service.exception.ErrorCode;
import com.nhngcmnh.example.identity_service.repository.UserRepository;
import com.nhngcmnh.example.identity_service.dto.response.AuthResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import java.util.Date;
import com.nhngcmnh.example.identity_service.dto.request.IntrospectRequest;
import com.nhngcmnh.example.identity_service.dto.response.IntrospectResponse;

@Service
public class AuthService {
    // Tạo chuỗi scope chuẩn từ roles, cách nhau bởi dấu cách
    private String builderScope(User user) {
        if (user.getRoles() == null || user.getRoles().isEmpty()) return "";
        return String.join(" ", user.getRoles().stream().map(Object::toString).toList());
    }
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    protected String JWT_SECRET;
    private static final long EXPIRATION_TIME = 60 * 60 * 1000; // 1 hour in ms

    public AuthResponse login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AppException(ErrorCode.INVALID_PASSWORD);
        }
        String token = generateToken(user);
        return AuthResponse.builder()
                .result(true)
                .token(token)
                .build();
    }

    public IntrospectResponse introspect(IntrospectRequest request) {
        boolean valid = true;
        String message = "Token hợp lệ";
        try {
            Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(JWT_SECRET.getBytes()))
                .build()
                .parseClaimsJws(request.getToken());
        } catch (ExpiredJwtException e) {
            valid = false;
            message = "Token đã hết hạn";
        } catch (JwtException e) {
            valid = false;
            message = "Token không hợp lệ";
        }
        return IntrospectResponse.builder().valid(valid).message(message).build();
    }

    private String generateToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);
        return Jwts.builder()
                .setSubject(user.getId())
                .claim("user", user)
                .claim("scope", builderScope(user))
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(Keys.hmacShaKeyFor(JWT_SECRET.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }
}
