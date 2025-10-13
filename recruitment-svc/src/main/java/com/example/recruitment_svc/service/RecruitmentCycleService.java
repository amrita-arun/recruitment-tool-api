package com.example.recruitment_svc.service;

import com.example.recruitment_svc.dtos.CreateRecruitmentCycleRequest;
import com.example.recruitment_svc.dtos.RecruitmentCycleDto;
import com.example.recruitment_svc.dtos.UpdateRecruitmentCycleRequest;
import com.example.recruitment_svc.errors.NotFoundException;
import com.example.recruitment_svc.model.Organization;
import com.example.recruitment_svc.model.OrganizationMember;
import com.example.recruitment_svc.model.RecruitmentCycle;
import com.example.recruitment_svc.model.User;
import com.example.recruitment_svc.repository.OrganizationMemberRepository;
import com.example.recruitment_svc.repository.OrganizationRepository;
import com.example.recruitment_svc.repository.RecruitmentCycleRepository;
import com.example.recruitment_svc.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecruitmentCycleService {

    private final RecruitmentCycleRepository recruitmentCycleRepository;
    private final OrganizationRepository organizationRepository;
    private final OrganizationMemberRepository organizationMemberRepository;
    private final UserRepository userRepository;

    public List<RecruitmentCycleDto> getOrganizationRecruitmentCycles(UUID organizationId) {
        User currentUser = getCurrentUser();
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new NotFoundException("Organization not found"));
        
        // Check if user is member of this organization
        organizationMemberRepository.findByUserAndOrganization(currentUser, organization)
                .orElseThrow(() -> new NotFoundException("You are not a member of this organization"));
        
        List<RecruitmentCycle> cycles = recruitmentCycleRepository.findByOrganization(organization);
        
        return cycles.stream()
                .map(cycle -> new RecruitmentCycleDto(
                        cycle.getId(),
                        cycle.getName(),
                        cycle.getDescription(),
                        cycle.getOrganization().getId(),
                        cycle.getOrganization().getName(),
                        cycle.getStatus(),
                        cycle.getCreatedAt(),
                        cycle.getUpdatedAt()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public RecruitmentCycleDto createRecruitmentCycle(UUID organizationId, CreateRecruitmentCycleRequest request) {
        User currentUser = getCurrentUser();
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new NotFoundException("Organization not found"));
        
        // Check if user is member of this organization
        OrganizationMember membership = organizationMemberRepository
                .findByUserAndOrganization(currentUser, organization)
                .orElseThrow(() -> new NotFoundException("You are not a member of this organization"));
        
        // Only admins can create recruitment cycles
        if (!"ADMIN".equals(membership.getRole())) {
            throw new RuntimeException("Only admins can create recruitment cycles");
        }
        
        RecruitmentCycle cycle = new RecruitmentCycle();
        cycle.setName(request.name());
        cycle.setDescription(request.description());
        cycle.setOrganization(organization);
        cycle.setStatus(RecruitmentCycle.RecruitmentCycleStatus.ACTIVE);
        
        RecruitmentCycle savedCycle = recruitmentCycleRepository.save(cycle);
        
        return new RecruitmentCycleDto(
                savedCycle.getId(),
                savedCycle.getName(),
                savedCycle.getDescription(),
                savedCycle.getOrganization().getId(),
                savedCycle.getOrganization().getName(),
                savedCycle.getStatus(),
                savedCycle.getCreatedAt(),
                savedCycle.getUpdatedAt()
        );
    }

    public RecruitmentCycleDto getRecruitmentCycle(UUID organizationId, UUID cycleId) {
        User currentUser = getCurrentUser();
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new NotFoundException("Organization not found"));
        
        // Check if user is member of this organization
        organizationMemberRepository.findByUserAndOrganization(currentUser, organization)
                .orElseThrow(() -> new NotFoundException("You are not a member of this organization"));
        
        RecruitmentCycle cycle = recruitmentCycleRepository.findById(cycleId)
                .orElseThrow(() -> new NotFoundException("Recruitment cycle not found"));
        
        if (!cycle.getOrganization().getId().equals(organizationId)) {
            throw new NotFoundException("Recruitment cycle does not belong to this organization");
        }
        
        return new RecruitmentCycleDto(
                cycle.getId(),
                cycle.getName(),
                cycle.getDescription(),
                cycle.getOrganization().getId(),
                cycle.getOrganization().getName(),
                cycle.getStatus(),
                cycle.getCreatedAt(),
                cycle.getUpdatedAt()
        );
    }

    @Transactional
    public RecruitmentCycleDto updateRecruitmentCycle(UUID organizationId, UUID cycleId, UpdateRecruitmentCycleRequest request) {
        User currentUser = getCurrentUser();
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new NotFoundException("Organization not found"));
        
        // Check if user is member of this organization
        OrganizationMember membership = organizationMemberRepository
                .findByUserAndOrganization(currentUser, organization)
                .orElseThrow(() -> new NotFoundException("You are not a member of this organization"));
        
        // Only admins can update recruitment cycles
        if (!"ADMIN".equals(membership.getRole())) {
            throw new RuntimeException("Only admins can update recruitment cycles");
        }
        
        RecruitmentCycle cycle = recruitmentCycleRepository.findById(cycleId)
                .orElseThrow(() -> new NotFoundException("Recruitment cycle not found"));
        
        if (!cycle.getOrganization().getId().equals(organizationId)) {
            throw new NotFoundException("Recruitment cycle does not belong to this organization");
        }
        
        if (request.name() != null && !request.name().trim().isEmpty()) {
            cycle.setName(request.name().trim());
        }
        
        if (request.description() != null) {
            cycle.setDescription(request.description().trim());
        }
        
        if (request.status() != null) {
            cycle.setStatus(request.status());
        }
        
        RecruitmentCycle savedCycle = recruitmentCycleRepository.save(cycle);
        
        return new RecruitmentCycleDto(
                savedCycle.getId(),
                savedCycle.getName(),
                savedCycle.getDescription(),
                savedCycle.getOrganization().getId(),
                savedCycle.getOrganization().getName(),
                savedCycle.getStatus(),
                savedCycle.getCreatedAt(),
                savedCycle.getUpdatedAt()
        );
    }

    @Transactional
    public void deleteRecruitmentCycle(UUID organizationId, UUID cycleId) {
        User currentUser = getCurrentUser();
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new NotFoundException("Organization not found"));
        
        // Check if user is member of this organization
        OrganizationMember membership = organizationMemberRepository
                .findByUserAndOrganization(currentUser, organization)
                .orElseThrow(() -> new NotFoundException("You are not a member of this organization"));
        
        // Only admins can delete recruitment cycles
        if (!"ADMIN".equals(membership.getRole())) {
            throw new RuntimeException("Only admins can delete recruitment cycles");
        }
        
        RecruitmentCycle cycle = recruitmentCycleRepository.findById(cycleId)
                .orElseThrow(() -> new NotFoundException("Recruitment cycle not found"));
        
        if (!cycle.getOrganization().getId().equals(organizationId)) {
            throw new NotFoundException("Recruitment cycle does not belong to this organization");
        }
        
        recruitmentCycleRepository.delete(cycle);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}