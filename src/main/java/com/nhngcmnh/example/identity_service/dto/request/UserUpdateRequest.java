package com.nhngcmnh.example.identity_service.dto.request;

import java.time.LocalDate;

import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;

public class UserUpdateRequest {
    @Id
    
    private String id;
    @Size(min=8,message="INVALID_PASSWORD")
    private String password;
    @Size(min=3,message = "INVALID_USERNAME")
    private String username;
    private String firstName;
    private String lastName;
    private LocalDate dob;

    // --- Getter & Setter ---
    public String getUsername() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }
}
