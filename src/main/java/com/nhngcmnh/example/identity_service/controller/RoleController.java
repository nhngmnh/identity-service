package com.nhngcmnh.example.identity_service.controller;

import com.nhngcmnh.example.identity_service.dto.request.RoleRequest;
import com.nhngcmnh.example.identity_service.dto.response.RoleResponse;
import com.nhngcmnh.example.identity_service.service.RoleService;
import com.nhngcmnh.example.identity_service.dto.request.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @PostMapping
    public ResponseEntity<ApiResponse<RoleResponse>> createRole(@RequestBody RoleRequest request) {
        RoleResponse data = roleService.createRole(request);
        return ResponseEntity.ok(ApiResponse.<RoleResponse>builder()
            .success(true)
            .message("Tạo role thành công")
            .code(200)
            .data(data)
            .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RoleResponse>>> getAllRoles() {
        List<RoleResponse> data = roleService.getAllRoles();
        return ResponseEntity.ok(ApiResponse.<List<RoleResponse>>builder()
            .success(true)
            .message("Lấy danh sách role thành công")
            .code(200)
            .data(data)
            .build());
    }

    @GetMapping("/{name}")
    public ResponseEntity<ApiResponse<RoleResponse>> getRole(@PathVariable String name) {
        RoleResponse data = roleService.getRole(name);
        return ResponseEntity.ok(ApiResponse.<RoleResponse>builder()
            .success(true)
            .message("Lấy role thành công")
            .code(200)
            .data(data)
            .build());
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<ApiResponse<Void>> deleteRole(@PathVariable String name) {
        roleService.deleteRole(name);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
            .success(true)
            .message("Xóa role thành công")
            .code(200)
            .build());
    }
}
