package com.example.recruitment_svc.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateRecruitmentCycleRequest(
        @NotBlank(message = "Recruitment cycle name is required")
        @Size(max = 255, message = "Name must not exceed 255 characters")
        String name,
        
        @Size(max = 1000, message = "Description must not exceed 1000 characters")
        String description
) {}