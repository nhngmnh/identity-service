package com.nhngcmnh.example.identity_service.dto.request;

import java.time.LocalDate;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level=AccessLevel.PRIVATE)
public class UserUpdateRequest {
    @Id
     String id;
    @Size(min=5,message="INVALID_PASSWORD")
    private String password;
    @Size(min=3,message = "INVALID_USERNAME")
     String username;
     String firstName;
     String lastName;
     LocalDate dob;
    // Đã loại bỏ roles, role sẽ được set mặc định trong service
}
