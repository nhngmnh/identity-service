package com.nhngcmnh.example.identity_service.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import com.nhngcmnh.example.identity_service.dto.request.UserCreationRequest;
import com.nhngcmnh.example.identity_service.dto.request.UserUpdateRequest;
import com.nhngcmnh.example.identity_service.dto.response.UserResponse;
import com.nhngcmnh.example.identity_service.entity.User;
import com.nhngcmnh.example.identity_service.service.UserService;

import jakarta.validation.Valid;

@RequestMapping("/users")
@RestController
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public UserResponse createUser(@RequestBody @Valid UserCreationRequest request){
        return userService.createUser(request);
    }

    @GetMapping
    public List<User> getUsers(){
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable String id){
        return userService.getUser(id);
    }

    // Endpoint cho user lấy thông tin của chính mình
    @GetMapping("/myInfo")
    public UserResponse getMyInfo(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        return userService.getUser(userId);
    }

    @PutMapping("/{id}")
    public UserResponse updateUser(@PathVariable String id, @RequestBody UserUpdateRequest request){
        return userService.updateUser(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable String id){
        userService.deleteUser(id);
    }
}
