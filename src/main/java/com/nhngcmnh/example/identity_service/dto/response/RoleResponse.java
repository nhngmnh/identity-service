package com.nhngcmnh.example.identity_service.dto.response;

import java.util.Set;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleResponse {
    private String name;
    private String description;
    private Set<PermissionResponse> permissions;
}
