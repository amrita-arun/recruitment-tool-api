package com.example.recruitment_svc.web;

import com.example.recruitment_svc.dtos.AddMemberRequest;
import com.example.recruitment_svc.dtos.CreateOrganizationRequest;
import com.example.recruitment_svc.dtos.OrganizationDto;
import com.example.recruitment_svc.dtos.OrganizationMemberDto;
import com.example.recruitment_svc.service.OrganizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    @GetMapping
    public List<OrganizationDto> getUserOrganizations() {
        return organizationService.getUserOrganizations();
    }

    @PostMapping
    public ResponseEntity<OrganizationDto> createOrganization(@Valid @RequestBody CreateOrganizationRequest request) {
        OrganizationDto organization = organizationService.createOrganization(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(organization);
    }

    @GetMapping("/{id}")
    public OrganizationDto getOrganization(@PathVariable UUID id) {
        return organizationService.getOrganization(id);
    }

    @GetMapping("/{id}/members")
    public List<OrganizationMemberDto> getOrganizationMembers(@PathVariable UUID id) {
        return organizationService.getOrganizationMembers(id);
    }

    @PostMapping("/{id}/members")
    public ResponseEntity<OrganizationMemberDto> addMember(
            @PathVariable UUID id,
            @Valid @RequestBody AddMemberRequest request) {
        OrganizationMemberDto member = organizationService.addMember(id, request.email(), request.role());
        return ResponseEntity.status(HttpStatus.CREATED).body(member);
    }

    @DeleteMapping("/{id}/members/{memberId}")
    public ResponseEntity<Void> removeMember(@PathVariable UUID id, @PathVariable UUID memberId) {
        organizationService.removeMember(id, memberId);
        return ResponseEntity.noContent().build();
    }
}