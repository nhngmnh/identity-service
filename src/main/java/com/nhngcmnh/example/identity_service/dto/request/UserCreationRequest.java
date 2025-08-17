package com.nhngcmnh.example.identity_service.dto.request;

import java.time.LocalDate;

import com.nhngcmnh.example.identity_service.validator.DobConstraint;

import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @Size(min = 3, message = "INVALID_INPUT")
    String username;
    @Size(min = 5, message = "INVALID_INPUT")
     String password;
     String firstName;
     String lastName;
     @DobConstraint(min=16, message = "INVALID_DOB")
     LocalDate dob;
   
}
