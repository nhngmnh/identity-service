package com.nhngcmnh.example.identity_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import com.nhngcmnh.example.identity_service.dto.request.LoginRequest;
import com.nhngcmnh.example.identity_service.dto.request.ApiResponse;
import com.nhngcmnh.example.identity_service.service.AuthService;
import com.nhngcmnh.example.identity_service.dto.response.AuthResponse;
import com.nhngcmnh.example.identity_service.dto.request.IntrospectRequest;
import com.nhngcmnh.example.identity_service.dto.response.IntrospectResponse;
import com.nhngcmnh.example.identity_service.dto.response.LogoutResponse;
import com.nhngcmnh.example.identity_service.dto.request.RefreshTokenRequest;
import com.nhngcmnh.example.identity_service.dto.response.RefreshTokenResponse;
import com.nhngcmnh.example.identity_service.dto.request.LogoutRequest;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody LoginRequest request) {
        AuthResponse authResponse = authService.login(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(ApiResponse.<AuthResponse>builder()
                .success(true)
                .data(authResponse)
                .message("Đăng nhập thành công")
                .code(0)
                .build());
    }

    @PostMapping("/introspect")
    public ResponseEntity<ApiResponse<IntrospectResponse>> introspect(@RequestBody IntrospectRequest request) {
        IntrospectResponse response = authService.introspect(request);
        return ResponseEntity.ok(ApiResponse.<IntrospectResponse>builder()
                .success(true)
                .data(response)
                .message("Introspect thành công")
                .code(0)
                .build());
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<LogoutResponse>> logout(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            LogoutResponse logoutResponse = LogoutResponse.builder()
                .success(false)
                .message("Missing or invalid Authorization header")
                .code(400)
                .build();
            return ResponseEntity.badRequest().body(ApiResponse.<LogoutResponse>builder()
                .success(false)
                .data(logoutResponse)
                .message("Missing or invalid Authorization header")
                .code(400)
                .build());
        }
        String token = authorizationHeader.substring(7); // Remove 'Bearer '
        LogoutRequest request = new LogoutRequest(token);
        authService.logout(request);
        LogoutResponse logoutResponse = LogoutResponse.builder()
            .success(true)
            .message("Logout thành công")
            .code(0)
            .build();
        return ResponseEntity.ok(ApiResponse.<LogoutResponse>builder()
            .success(true)
            .data(logoutResponse)
            .message("Logout thành công")
            .code(0)
            .build());
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<RefreshTokenResponse>> refreshToken(@RequestHeader("Authorization") String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            RefreshTokenResponse response = RefreshTokenResponse.builder()
                .success(false)
                .message("Missing or invalid Authorization header")
                .code(400)
                .token(null)
                .build();
            return ResponseEntity.badRequest().body(ApiResponse.<RefreshTokenResponse>builder()
                .success(false)
                .data(response)
                .message(response.getMessage())
                .code(response.getCode())
                .build());
        }
        String token = authorizationHeader.substring(7); // Remove 'Bearer '
        RefreshTokenRequest request = new RefreshTokenRequest(token);
        RefreshTokenResponse response = authService.refreshToken(request);
        return ResponseEntity.ok(ApiResponse.<RefreshTokenResponse>builder()
            .success(response.isSuccess())
            .data(response)
            .message(response.getMessage())
            .code(response.getCode())
            .build());
    }
}
