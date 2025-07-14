package com.nhngcmnh.example.identity_service.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nhngcmnh.example.identity_service.entity.User;
import com.nhngcmnh.example.identity_service.exception.AppException;
import com.nhngcmnh.example.identity_service.exception.ErrorCode;
import com.nhngcmnh.example.identity_service.mapper.UserMapper;
import com.nhngcmnh.example.identity_service.repository.UserRepository;
import com.nhngcmnh.example.identity_service.dto.request.UserCreationRequest;
import com.nhngcmnh.example.identity_service.dto.request.UserUpdateRequest;
import com.nhngcmnh.example.identity_service.dto.response.UserResponse;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    // ✅ Tạo user mới, kiểm tra trùng username trước khi lưu
    public UserResponse createUser(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User user = userMapper.toUser(request);
        
      userRepository.save(user); 
        return userMapper.toUserResponse(user);
        
    }

    // ✅ Lấy danh sách tất cả user
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    // ✅ Lấy thông tin user theo ID
    public UserResponse getUser(String id) {
        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User không tồn tại với ID: " + id)));
    }

    // ✅ Xóa user theo ID
    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new NoSuchElementException("User không tồn tại với ID: " + id);
        }
        userRepository.deleteById(id);
    }

    // ✅ Cập nhật user theo ID
    public UserResponse updateUser(String id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User không tồn tại với ID: " + id));

        userMapper.updateUser(user, request);

     userRepository.save(user);
     return userMapper.toUserResponse(user);
    }
}
