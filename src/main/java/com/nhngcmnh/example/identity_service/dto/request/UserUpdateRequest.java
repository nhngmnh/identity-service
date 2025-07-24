package com.nhngcmnh.example.identity_service.dto.request;

import java.time.LocalDate;
import java.util.List;

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
     List<String> roles; // Assuming roles are represented as a list of role IDs or names
}
