package com.example.recruitment_svc.web;

import com.example.recruitment_svc.dtos.CreateRecruitmentCycleRequest;
import com.example.recruitment_svc.dtos.RecruitmentCycleDto;
import com.example.recruitment_svc.dtos.UpdateRecruitmentCycleRequest;
import com.example.recruitment_svc.service.RecruitmentCycleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/organizations/{organizationId}/recruitment-cycles")
@RequiredArgsConstructor
public class RecruitmentCycleController {

    private final RecruitmentCycleService recruitmentCycleService;

    @GetMapping
    public List<RecruitmentCycleDto> getOrganizationRecruitmentCycles(@PathVariable UUID organizationId) {
        return recruitmentCycleService.getOrganizationRecruitmentCycles(organizationId);
    }

    @PostMapping
    public ResponseEntity<RecruitmentCycleDto> createRecruitmentCycle(
            @PathVariable UUID organizationId,
            @Valid @RequestBody CreateRecruitmentCycleRequest request) {
        RecruitmentCycleDto cycle = recruitmentCycleService.createRecruitmentCycle(organizationId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(cycle);
    }

    @GetMapping("/{cycleId}")
    public RecruitmentCycleDto getRecruitmentCycle(
            @PathVariable UUID organizationId,
            @PathVariable UUID cycleId) {
        return recruitmentCycleService.getRecruitmentCycle(organizationId, cycleId);
    }

    @PutMapping("/{cycleId}")
    public RecruitmentCycleDto updateRecruitmentCycle(
            @PathVariable UUID organizationId,
            @PathVariable UUID cycleId,
            @Valid @RequestBody UpdateRecruitmentCycleRequest request) {
        return recruitmentCycleService.updateRecruitmentCycle(organizationId, cycleId, request);
    }

    @DeleteMapping("/{cycleId}")
    public ResponseEntity<Void> deleteRecruitmentCycle(
            @PathVariable UUID organizationId,
            @PathVariable UUID cycleId) {
        recruitmentCycleService.deleteRecruitmentCycle(organizationId, cycleId);
        return ResponseEntity.noContent().build();
    }
}