package com.nhngcmnh.example.identity_service.service;

import com.nhngcmnh.example.identity_service.dto.request.PermissionRequest;
import com.nhngcmnh.example.identity_service.dto.response.PermissionResponse;
import com.nhngcmnh.example.identity_service.entity.Permission;
import com.nhngcmnh.example.identity_service.exception.AppException;
import com.nhngcmnh.example.identity_service.exception.ErrorCode;
import com.nhngcmnh.example.identity_service.mapper.PermissionMapper;
import com.nhngcmnh.example.identity_service.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionService {
    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    @Transactional
    public PermissionResponse createPermission(PermissionRequest request) {
        Permission permission = permissionMapper.toPermission(request);
        permission = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    public List<PermissionResponse> getAllPermissions() {
        return permissionRepository.findAll().stream().map(permissionMapper::toPermissionResponse).collect(Collectors.toList());
    }

    public PermissionResponse getPermission(String name) {
        Permission permission = permissionRepository.findById(name)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
        return permissionMapper.toPermissionResponse(permission);
    }

    public void deletePermission(String name) {
        permissionRepository.deleteById(name);
    }
}
