package com.nhngcmnh.example.identity_service.service;

import com.nhngcmnh.example.identity_service.dto.request.RoleRequest;
import com.nhngcmnh.example.identity_service.dto.response.RoleResponse;
import com.nhngcmnh.example.identity_service.entity.Permission;
import com.nhngcmnh.example.identity_service.entity.Role;
import com.nhngcmnh.example.identity_service.exception.AppException;
import com.nhngcmnh.example.identity_service.exception.ErrorCode;
import com.nhngcmnh.example.identity_service.mapper.RoleMapper;
import com.nhngcmnh.example.identity_service.repository.PermissionRepository;
import com.nhngcmnh.example.identity_service.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RoleMapper roleMapper;

    @Transactional
    public RoleResponse createRole(RoleRequest request) {
        Role role = new Role();
        role.setName(request.getName());
        role.setDescription(request.getDescription());
        // Map permissions by id
        Set<Permission> permissions = new HashSet<>();
        if (request.getPermissions() != null) {
            for (String permissionId : request.getPermissions()) {
                Permission permission = permissionRepository.findById(permissionId)
                        .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
                permissions.add(permission);
            }
        }
        role.setPermissions(permissions);
        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAllFetchPermissions().stream().map(roleMapper::toRoleResponse).collect(Collectors.toList());
    }

    public RoleResponse getRole(String name) {
        Role role = roleRepository.findByIdFetchPermissions(name)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));
        return roleMapper.toRoleResponse(role);
    }

    public void deleteRole(String name) {
        roleRepository.deleteById(name);
    }
}
