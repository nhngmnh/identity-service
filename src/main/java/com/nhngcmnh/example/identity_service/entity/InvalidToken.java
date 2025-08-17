package com.nhngcmnh.example.identity_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class InvalidToken {
    @Id
    private String token;
    private Date expiredTime;
}
