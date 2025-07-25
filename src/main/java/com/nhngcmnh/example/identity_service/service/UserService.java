
package com.nhngcmnh.example.identity_service.service;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nhngcmnh.example.identity_service.entity.Role;
import com.nhngcmnh.example.identity_service.entity.User;
import com.nhngcmnh.example.identity_service.exception.AppException;
import com.nhngcmnh.example.identity_service.exception.ErrorCode;
import com.nhngcmnh.example.identity_service.mapper.UserMapper;
import com.nhngcmnh.example.identity_service.repository.UserRepository;
import com.nhngcmnh.example.identity_service.repository.RoleRepository;
import com.nhngcmnh.example.identity_service.dto.request.UserCreationRequest;
import com.nhngcmnh.example.identity_service.dto.request.UserUpdateRequest;
import com.nhngcmnh.example.identity_service.dto.response.UserResponse;

import org.springframework.security.crypto.password.PasswordEncoder;
@Service
public class UserService {
    @Autowired
    private RoleRepository roleRepository;


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    // ✅ Tạo user mới, kiểm tra trùng username trước khi lưu
    public UserResponse createUser(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User user = userMapper.toUser(request);
        // Mã hóa mật khẩu trước khi lưu
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        // Đảm bảo trả về response có đủ thông tin role (và permission nếu cần)
        User savedUser = userRepository.findById(user.getId()).orElse(user);
        return userMapper.toUserResponse(savedUser);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    // ✅ Lấy thông tin user theo ID (không phân quyền ở đây)
    public UserResponse getUser(String id) {
        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User không tồn tại với ID: " + id)));
    }

    public UserResponse getMyInfo(String id) {
        return getUser(id);
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
        // Nếu có mật khẩu mới, mã hóa trước khi lưu
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        // Ánh xạ role từ request sang entity nếu có trường roles
        if (request.getRoles() != null) {
            Set<Role> roles = new HashSet<>();
            for (String roleName : request.getRoles()) {
                Role role = roleRepository.findById(roleName)
                    .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
                roles.add(role);
            }
            user.setRoles(roles);
        }
        return userMapper.toUserResponse(user);
    }
}
