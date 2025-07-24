package com.nhngcmnh.example.identity_service.config;

import com.nhngcmnh.example.identity_service.dto.request.ApiResponse;
import com.nhngcmnh.example.identity_service.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setStatus(ErrorCode.UNAUTHENTICATION.getStatus().value());
        response.setContentType("application/json");
        ApiResponse<Object> body = new ApiResponse<>(
            false,
            ErrorCode.UNAUTHENTICATION.getMessage(),
            ErrorCode.UNAUTHENTICATION.getCode(),
            null
        );
        new ObjectMapper().writeValue(response.getOutputStream(), body);
    }
}
