package com.example.recruitment_svc.dtos;

import com.example.recruitment_svc.model.Status;
import com.fasterxml.jackson.databind.JsonNode;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record ApplicantDetailDto(UUID id, String name, String email, String phone, String location, String major, String year,
                                 BigDecimal gpa, Status status, JsonNode raw, OffsetDateTime created_at, String resume_url, JsonNode answers) {
}
