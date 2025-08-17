package com.nhngcmnh.example.identity_service.service;

import com.nhngcmnh.example.identity_service.entity.InvalidToken;
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
import lombok.extern.slf4j.Slf4j;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import java.util.Date;
import java.util.StringJoiner;

import com.nhngcmnh.example.identity_service.dto.request.IntrospectRequest;
import com.nhngcmnh.example.identity_service.dto.response.IntrospectResponse;
import com.nhngcmnh.example.identity_service.repository.InvalidTokenRepository;
import io.jsonwebtoken.Claims;
import com.nhngcmnh.example.identity_service.dto.request.RefreshTokenRequest;
import com.nhngcmnh.example.identity_service.dto.response.RefreshTokenResponse;
import com.nhngcmnh.example.identity_service.dto.request.LogoutRequest;

@Service
@Slf4j
public class AuthService {
    // Tạo chuỗi scope chuẩn từ roles, cách nhau bởi dấu cách
    private String builderScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        log.info("[builderScope] user.roles = {}", user.getRoles());
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            log.info("[builderScope] Không có roles, trả về chuỗi rỗng");
            return "";
        }
        for (var role : user.getRoles()) {
            log.info("[builderScope] Đang xử lý role: {}", role);
            if (role == null || role.getName() == null) {
                log.info("[builderScope] Role null hoặc không có tên, bỏ qua");
                continue;
            }
            log.info("[builderScope] Thêm role: ROLE_{}", role.getName());
            stringJoiner.add("ROLE_" + role.getName());
            if (role.getPermissions() != null && !role.getPermissions().isEmpty()) {
                log.info("[builderScope] Role có permissions: {}", role.getPermissions());
                for (var permission : role.getPermissions()) {
                    log.info("[builderScope] Đang xử lý permission: {}", permission);
                    if (permission != null && permission.getName() != null) {
                        log.info("[builderScope] Thêm permission: {}", permission.getName());
                        stringJoiner.add(permission.getName());
                    } else {
                        log.info("[builderScope] Permission null hoặc không có tên, bỏ qua");
                    }
                }
            } else {
                log.info("[builderScope] Role không có permissions hoặc rỗng");
            }
        }
        log.info("[builderScope] scope cuối cùng: {}", stringJoiner.toString());
        return stringJoiner.toString();
    }
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private InvalidTokenRepository invalidTokenRepository;

    @Value("${jwt.secret}")
    protected String JWT_SECRET;
    private static final long EXPIRATION_TIME = 60 * 60 * 1000; // 1 hour in ms

    public AuthResponse login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTS));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AppException(ErrorCode.INVALID_PASSWORD);
        }
        log.info("[login] user: {}", user);
        String token = generateToken(user);
        log.info("đến đoạn này không lỗi", token);
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
        String randomId = java.util.UUID.randomUUID().toString();
        return Jwts.builder()
                .setSubject(user.getId())
                .claim("user", user)
                .claim("scope", builderScope(user))
                .claim("randomId", randomId)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(Keys.hmacShaKeyFor(JWT_SECRET.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public void logout(LogoutRequest request) {
        String token = request.getToken();
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(JWT_SECRET.getBytes()))
            .build()
            .parseClaimsJws(token)
            .getBody();
        String randomId = claims.get("randomId", String.class);
        Date expiryDate = claims.getExpiration();
        if (!invalidTokenRepository.existsById(randomId)) {
            InvalidToken entity = new InvalidToken(randomId, expiryDate);
            invalidTokenRepository.save(entity);
            log.info("[LOGOUT] Token invalidated: {}", randomId);
        } else {
            log.info("[LOGOUT] Token already invalidated: {}", randomId);
        }
    }

    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
        String token = request.getToken();
        Claims claims;
        try {
            claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(JWT_SECRET.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
        } catch (Exception e) {
            return RefreshTokenResponse.builder()
                .success(false)
                .message("Token không hợp lệ")
                .code(400)
                .token(null)
                .build();
        }
        String randomId = claims.get("randomId", String.class);
        Date expiryDate = claims.getExpiration();
        String userId = claims.getSubject();
        // Invalidate old token
        if (!invalidTokenRepository.existsById(randomId)) {
            InvalidToken entity = new InvalidToken(randomId, expiryDate);
            invalidTokenRepository.save(entity);
            log.info("[REFRESH] Token invalidated: {}", randomId);
        } else {
            log.info("[REFRESH] Token already invalidated: {}", randomId);
        }
        // Check user exists
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return RefreshTokenResponse.builder()
                .success(false)
                .message("User không tồn tại")
                .code(404)
                .token(null)
                .build();
        }
        // Generate new token
        String newToken = generateToken(user);
        return RefreshTokenResponse.builder()
            .success(true)
            .message("Refresh token thành công")
            .code(0)
            .token(newToken)
            .build();
    }
}
