package com.nhngcmnh.example.identity_service.controller;

import com.nhngcmnh.example.identity_service.dto.request.PermissionRequest;
import com.nhngcmnh.example.identity_service.dto.response.PermissionResponse;
import com.nhngcmnh.example.identity_service.service.PermissionService;

import org.springframework.http.ResponseEntity;
import com.nhngcmnh.example.identity_service.dto.request.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/permissions")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    @PostMapping
    public ResponseEntity<ApiResponse<PermissionResponse>> createPermission(@RequestBody PermissionRequest request) {
        PermissionResponse data = permissionService.createPermission(request);
        return ResponseEntity.ok(ApiResponse.<PermissionResponse>builder()
            .success(true)
            .message("Tạo permission thành công")
            .code(200)
            .data(data)
            .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PermissionResponse>>> getAllPermissions() {
        List<PermissionResponse> data = permissionService.getAllPermissions();
        return ResponseEntity.ok(ApiResponse.<List<PermissionResponse>>builder()
            .success(true)
            .message("Lấy danh sách permission thành công")
            .code(200)
            .data(data)
            .build());
    }

    @GetMapping("/{name}")
    public ResponseEntity<ApiResponse<PermissionResponse>> getPermission(@PathVariable String name) {
        PermissionResponse data = permissionService.getPermission(name);
        return ResponseEntity.ok(ApiResponse.<PermissionResponse>builder()
            .success(true)
            .message("Lấy permission thành công")
            .code(200)
            .data(data)
            .build());
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<ApiResponse<Void>> deletePermission(@PathVariable String name) {
        permissionService.deletePermission(name);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
            .success(true)
            .message("Xóa permission thành công")
            .code(200)
            .build());
    }
}
