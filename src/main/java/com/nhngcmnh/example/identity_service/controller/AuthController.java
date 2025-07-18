
package com.nhngcmnh.example.identity_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nhngcmnh.example.identity_service.dto.request.LoginRequest;
import com.nhngcmnh.example.identity_service.dto.request.ApiResponse;
import com.nhngcmnh.example.identity_service.service.AuthService;
import com.nhngcmnh.example.identity_service.dto.response.AuthResponse;

@RestController
@RequestMapping("/auth")
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
}
