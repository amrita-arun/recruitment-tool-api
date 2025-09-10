package com.example.recruitment_svc.web;

import com.example.recruitment_svc.errors.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.Map;

@RestControllerAdvice
public class ApiErrors {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> notFound(NotFoundException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> badRequest(MethodArgumentNotValidException e) {
        var errors = new ArrayList<Map<String, String>>();
        e.getBindingResult().getFieldErrors().forEach(fe ->
                errors.add(Map.of("field", fe.getField(), "message", fe.getDefaultMessage()))
        );
        return Map.of("error", "validation_failed", "details", "errors");
    }

}
