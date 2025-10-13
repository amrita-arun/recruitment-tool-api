package com.example.recruitment_svc.dtos;

import java.time.OffsetDateTime;
import java.util.UUID;

public record OrganizationMemberDto(
        UUID id,
        UUID userId,
        String userName,
        String userEmail,
        String role,
        OffsetDateTime joinedAt
) {}