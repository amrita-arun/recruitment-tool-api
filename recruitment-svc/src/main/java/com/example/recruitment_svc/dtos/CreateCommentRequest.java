package com.example.recruitment_svc.dtos;

import jakarta.validation.constraints.NotBlank;

public record CreateCommentRequest(@NotBlank String author, @NotBlank String body) {
}
