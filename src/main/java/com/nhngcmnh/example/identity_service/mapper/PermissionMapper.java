package com.nhngcmnh.example.identity_service.mapper;

import com.nhngcmnh.example.identity_service.entity.Permission;
import com.nhngcmnh.example.identity_service.dto.request.PermissionRequest;
import com.nhngcmnh.example.identity_service.dto.response.PermissionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    PermissionMapper INSTANCE = Mappers.getMapper(PermissionMapper.class);
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
}
