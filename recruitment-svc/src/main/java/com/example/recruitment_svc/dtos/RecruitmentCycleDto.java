package com.example.recruitment_svc.dtos;

import com.example.recruitment_svc.model.RecruitmentCycle.RecruitmentCycleStatus;
import java.time.OffsetDateTime;
import java.util.UUID;

public record RecruitmentCycleDto(
        UUID id,
        String name,
        String description,
        UUID organizationId,
        String organizationName,
        RecruitmentCycleStatus status,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {}