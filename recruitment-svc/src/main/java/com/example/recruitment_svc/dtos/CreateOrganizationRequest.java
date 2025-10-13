package com.example.recruitment_svc.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateOrganizationRequest(
        @NotBlank(message = "Organization name is required")
        @Size(max = 255, message = "Organization name must not exceed 255 characters")
        String name,
        
        @Size(max = 500, message = "Description must not exceed 500 characters")
        String description
) {}