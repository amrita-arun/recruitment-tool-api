package com.example.recruitment_svc.controller;

import com.example.recruitment_svc.service.ApplicantService;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/applicants")
public class ApplicantController {
    private final ApplicantService applicantService;

    public ApplicantController(ApplicantService applicantService) {
        this.applicantService = applicantService;
    }

    @PostMapping("/import")
    public ResponseEntity<String> importApplicants(@RequestParam("file") MultipartFile file) {
        try {
            int count = applicantService.importCsv(file.getInputStream());
            return ResponseEntity.ok("Successfully imported " + count + " applicants.");
        } catch (IOException | CsvValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to import applicants: " + e.getMessage());
        }
    }
}
