package com.nhngcmnh.example.identity_service.dto.request;

import lombok.Data;

@Data
public class IntrospectRequest {
    private String token;
}
