package com.nhngcmnh.example.identity_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nhngcmnh.example.identity_service.entity.User;
import com.nhngcmnh.example.identity_service.repository.UserRepository;
import com.nhngcmnh.example.identity_service.dto.request.UserCreationRequest;
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User createRequest(UserCreationRequest request){
        User user=new User();
        user.setDob(request.getDob());
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        return userRepository.save(user); 
    }
}
