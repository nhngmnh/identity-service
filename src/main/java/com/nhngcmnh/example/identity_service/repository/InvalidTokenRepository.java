package com.nhngcmnh.example.identity_service.repository;

import com.nhngcmnh.example.identity_service.entity.InvalidToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvalidTokenRepository extends JpaRepository<InvalidToken, String> {
    boolean existsById(String id);
}

