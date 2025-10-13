package com.example.recruitment_svc.dtos;

import java.time.OffsetDateTime;
import java.util.UUID;

public record OrganizationDto(
        UUID id,
        String name,
        String description,
        String userRole,
        OffsetDateTime createdAt
) {}