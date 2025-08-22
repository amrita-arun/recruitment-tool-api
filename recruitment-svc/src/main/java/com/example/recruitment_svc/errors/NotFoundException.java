package com.example.recruitment_svc.errors;

public class NotFoundException  extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}

