package com.nhngcmnh.example.identity_service.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nhngcmnh.example.identity_service.entity.User;
import com.nhngcmnh.example.identity_service.repository.UserRepository;
import com.nhngcmnh.example.identity_service.dto.request.UserCreationRequest;
import com.nhngcmnh.example.identity_service.dto.request.UserUpdateRequest;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // ✅ Tạo user mới, kiểm tra trùng username trước khi lưu
    public User createUser(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username đã tồn tại");
        }

        User user = new User();
        user.setDob(request.getDob());
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        return userRepository.save(user); 
    }

    // ✅ Lấy danh sách tất cả user
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    // ✅ Lấy thông tin user theo ID
    public User getUser(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User không tồn tại với ID: " + id));
    }

    // ✅ Xóa user theo ID
    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new NoSuchElementException("User không tồn tại với ID: " + id);
        }
        userRepository.deleteById(id);
    }

    // ✅ Cập nhật user theo ID
    public User updateUser(String id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User không tồn tại với ID: " + id));

        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDob(request.getDob());

        return userRepository.save(user);
    }
}
