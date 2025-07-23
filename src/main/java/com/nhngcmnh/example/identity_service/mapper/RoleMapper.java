package com.nhngcmnh.example.identity_service.mapper;

import com.nhngcmnh.example.identity_service.entity.Role;
import com.nhngcmnh.example.identity_service.dto.request.RoleRequest;
import com.nhngcmnh.example.identity_service.dto.response.RoleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);
    RoleResponse toRoleResponse(Role role);
}
