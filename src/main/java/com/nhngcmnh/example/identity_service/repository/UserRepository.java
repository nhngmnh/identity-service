package com.nhngcmnh.example.identity_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nhngcmnh.example.identity_service.entity.User;


@Repository
public interface UserRepository extends JpaRepository< User, String > {    
    boolean existsByUsername(String username);
    boolean existsById(@org.springframework.lang.NonNull String id);
    java.util.Optional<User> findByUsername(String username);
} 