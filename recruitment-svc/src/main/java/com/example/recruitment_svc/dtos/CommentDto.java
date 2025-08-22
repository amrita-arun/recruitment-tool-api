package com.example.recruitment_svc.dtos;

import com.example.recruitment_svc.model.Applicant;

import java.time.OffsetDateTime;
import java.util.UUID;

public record CommentDto(UUID id, UUID appId, String name, String body, OffsetDateTime timestamp) {
}
