package com.example.recruitment_svc.dtos;

import com.example.recruitment_svc.model.Status;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record ApplicantSummaryDto (
        UUID id, String name, String email, String major, String year, BigDecimal gpa, Status status, OffsetDateTime createdAt
) {}