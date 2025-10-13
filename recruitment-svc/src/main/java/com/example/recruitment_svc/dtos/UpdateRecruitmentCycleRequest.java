package com.example.recruitment_svc.dtos;

import com.example.recruitment_svc.model.RecruitmentCycle.RecruitmentCycleStatus;
import jakarta.validation.constraints.Size;

public record UpdateRecruitmentCycleRequest(
        @Size(max = 255, message = "Name must not exceed 255 characters")
        String name,
        
        @Size(max = 1000, message = "Description must not exceed 1000 characters")
        String description,
        
        RecruitmentCycleStatus status
) {}