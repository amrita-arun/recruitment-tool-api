package com.example.recruitment_svc.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AddMemberRequest(
        @Email(message = "Invalid email format")
        @NotBlank(message = "Email is required")
        String email,
        
        @NotBlank(message = "Role is required")
        String role
) {}