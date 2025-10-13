package com.example.recruitment_svc.dtos;

public record AuthResponse(
        String token,
        String email,
        String name
) {}