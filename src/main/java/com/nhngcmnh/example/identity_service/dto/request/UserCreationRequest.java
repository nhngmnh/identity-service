package com.nhngcmnh.example.identity_service.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Size;

public class UserCreationRequest {
    @Size(min=3, message="toi thieu 3 ky tu")
    private String username;
    @Size(min=8, message="toi thieu 8 ky tu")
    private String password;
    private String firstName;
    private String lastName;
    private LocalDate dob;

    // --- Getter & Setter ---

    public String getUsername() {
        return username;
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
