package com.example.recruitment_svc.dtos;
import com.example.recruitment_svc.model.Status;
import jakarta.validation.constraints.NotNull;

public record UpdateStatusRequest (@NotNull Status status){ }
