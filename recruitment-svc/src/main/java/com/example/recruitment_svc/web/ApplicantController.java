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
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApplicantController {
    private final ApplicantService svc;

    // Organization-level applicant management
    @GetMapping("/organizations/{organizationId}/applicants")
    public Page<ApplicantSummaryDto> listByOrganization(
            @PathVariable UUID organizationId,
            @RequestParam(value="query", required = false) String query,
            @RequestParam(value="status", required=false) com.example.recruitment_svc.model.Status status,
            @org.springframework.data.web.PageableDefault(size=25, sort="createdAt", direction= Sort.Direction.DESC)
            org.springframework.data.domain.Pageable pageable
    ) {
        return svc.listByOrganization(organizationId, query, status, pageable);
    }

    @GetMapping("/organizations/{organizationId}/applicants/{id}")
    public ApplicantDetailDto getByOrganization(@PathVariable UUID organizationId, @PathVariable UUID id) {
        return svc.getByOrganization(organizationId, id);
    }

    @PatchMapping("/organizations/{organizationId}/applicants/{id}/status")
    public ApplicantDetailDto updateStatusByOrganization(
            @PathVariable UUID organizationId,
            @PathVariable UUID id,
            @Valid @RequestBody UpdateStatusRequest body
    ) {
        return svc.updateStatusByOrganization(organizationId, id, body.status());
    }

    // Recruitment cycle-level applicant management
    @GetMapping("/organizations/{organizationId}/recruitment-cycles/{recruitmentCycleId}/applicants")
    public Page<ApplicantSummaryDto> listByRecruitmentCycle(
            @PathVariable UUID organizationId,
            @PathVariable UUID recruitmentCycleId,
            @RequestParam(value="query", required = false) String query,
            @RequestParam(value="status", required=false) com.example.recruitment_svc.model.Status status,
            @org.springframework.data.web.PageableDefault(size=25, sort="createdAt", direction= Sort.Direction.DESC)
            org.springframework.data.domain.Pageable pageable
    ) {
        return svc.listByRecruitmentCycle(recruitmentCycleId, query, status, pageable);
    }

    @GetMapping("/organizations/{organizationId}/recruitment-cycles/{recruitmentCycleId}/applicants/{id}")
    public ApplicantDetailDto getByRecruitmentCycle(
            @PathVariable UUID organizationId,
            @PathVariable UUID recruitmentCycleId,
            @PathVariable UUID id
    ) {
        return svc.getByRecruitmentCycle(recruitmentCycleId, id);
    }

    @PostMapping("/organizations/{organizationId}/recruitment-cycles/{recruitmentCycleId}/applicants/import")
    public ResponseEntity<String> importApplicants(
            @PathVariable UUID organizationId,
            @PathVariable UUID recruitmentCycleId,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            int count = svc.importCsv(recruitmentCycleId, file.getInputStream());
            return ResponseEntity.ok("Successfully imported " + count + " applicants.");
        } catch (IOException | CsvValidationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to import applicants: " + e.getMessage());
        }
    }

    @PatchMapping("/organizations/{organizationId}/recruitment-cycles/{recruitmentCycleId}/applicants/{id}/status")
    public ApplicantDetailDto updateStatusByRecruitmentCycle(
            @PathVariable UUID organizationId,
            @PathVariable UUID recruitmentCycleId,
            @PathVariable UUID id,
            @Valid @RequestBody UpdateStatusRequest body
    ) {
        return svc.updateStatusByRecruitmentCycle(recruitmentCycleId, id, body.status());
    }
}
