package com.example.recruitment_svc.web;

import com.example.recruitment_svc.dtos.ApplicantDetailDto;
import com.example.recruitment_svc.dtos.ApplicantSummaryDto;
import com.example.recruitment_svc.dtos.UpdateStatusRequest;
import com.example.recruitment_svc.service.ApplicantService;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/applicants")
@RequiredArgsConstructor
public class ApplicantController {
    private final ApplicantService svc;


    @GetMapping
    public Page<ApplicantSummaryDto> list(
            @RequestParam(value="query", required = false) String query,
            @RequestParam(value="status", required=false) com.example.recruitment_svc.model.Status status,
            @org.springframework.data.web.PageableDefault(size=25, sort="createdAt", direction= Sort.Direction.DESC)
            org.springframework.data.domain.Pageable pageable
    ) {
        return svc.list(query, status, pageable);
    }

    @GetMapping("/{id}")
    public ApplicantDetailDto get(@PathVariable UUID id) {
        return svc.get(id);
    }

    @PostMapping("/import")
    public ResponseEntity<String> importApplicants(@RequestParam("file") MultipartFile file) {
        try {
            int count = svc.importCsv(file.getInputStream());
            return ResponseEntity.ok("Successfully imported " + count + " applicants.");
        } catch (IOException | CsvValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to import applicants: " + e.getMessage());
        }
    }

    @PatchMapping("/{id}/status")
    public ApplicantDetailDto updateStatus(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateStatusRequest body
            ) {
        return svc.updateStatus(id, body.status());
    }




}
