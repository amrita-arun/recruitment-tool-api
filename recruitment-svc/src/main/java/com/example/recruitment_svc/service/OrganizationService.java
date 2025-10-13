package com.example.recruitment_svc.service;

import com.example.recruitment_svc.dtos.CreateOrganizationRequest;
import com.example.recruitment_svc.dtos.OrganizationDto;
import com.example.recruitment_svc.dtos.OrganizationMemberDto;
import com.example.recruitment_svc.errors.NotFoundException;
import com.example.recruitment_svc.model.Organization;
import com.example.recruitment_svc.model.OrganizationMember;
import com.example.recruitment_svc.model.User;
import com.example.recruitment_svc.repository.OrganizationMemberRepository;
import com.example.recruitment_svc.repository.OrganizationRepository;
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
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationMemberRepository organizationMemberRepository;
    private final UserRepository userRepository;

    public List<OrganizationDto> getUserOrganizations() {
        User currentUser = getCurrentUser();
        List<OrganizationMember> memberships = organizationMemberRepository.findByUser(currentUser);
        
        return memberships.stream()
                .map(membership -> new OrganizationDto(
                        membership.getOrganization().getId(),
                        membership.getOrganization().getName(),
                        membership.getOrganization().getDescription(),
                        membership.getRole(),
                        membership.getOrganization().getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public OrganizationDto createOrganization(CreateOrganizationRequest request) {
        User currentUser = getCurrentUser();
        
        Organization organization = new Organization();
        organization.setName(request.name());
        organization.setDescription(request.description());
        
        Organization savedOrganization = organizationRepository.save(organization);
        
        // Add creator as admin
        OrganizationMember adminMember = new OrganizationMember();
        adminMember.setUser(currentUser);
        adminMember.setOrganization(savedOrganization);
        adminMember.setRole("ADMIN");
        organizationMemberRepository.save(adminMember);
        
        return new OrganizationDto(
                savedOrganization.getId(),
                savedOrganization.getName(),
                savedOrganization.getDescription(),
                "ADMIN",
                savedOrganization.getCreatedAt()
        );
    }

    public OrganizationDto getOrganization(UUID organizationId) {
        User currentUser = getCurrentUser();
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new NotFoundException("Organization not found"));
        
        // Check if user is member of this organization
        OrganizationMember membership = organizationMemberRepository
                .findByUserAndOrganization(currentUser, organization)
                .orElseThrow(() -> new NotFoundException("You are not a member of this organization"));
        
        return new OrganizationDto(
                organization.getId(),
                organization.getName(),
                organization.getDescription(),
                membership.getRole(),
                organization.getCreatedAt()
        );
    }

    public List<OrganizationMemberDto> getOrganizationMembers(UUID organizationId) {
        User currentUser = getCurrentUser();
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new NotFoundException("Organization not found"));
        
        // Check if user is member of this organization
        organizationMemberRepository.findByUserAndOrganization(currentUser, organization)
                .orElseThrow(() -> new NotFoundException("You are not a member of this organization"));
        
        List<OrganizationMember> members = organizationMemberRepository.findByOrganization(organization);
        
        return members.stream()
                .map(member -> new OrganizationMemberDto(
                        member.getId(),
                        member.getUser().getId(),
                        member.getUser().getName(),
                        member.getUser().getEmail(),
                        member.getRole(),
                        member.getJoinedAt()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public OrganizationMemberDto addMember(UUID organizationId, String email, String role) {
        User currentUser = getCurrentUser();
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new NotFoundException("Organization not found"));
        
        // Check if current user is admin
        OrganizationMember currentMembership = organizationMemberRepository
                .findByUserAndOrganization(currentUser, organization)
                .orElseThrow(() -> new NotFoundException("You are not a member of this organization"));
        
        if (!"ADMIN".equals(currentMembership.getRole())) {
            throw new RuntimeException("Only admins can add members");
        }
        
        User newMember = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found with email: " + email));
        
        // Check if user is already a member
        if (organizationMemberRepository.findByUserAndOrganization(newMember, organization).isPresent()) {
            throw new RuntimeException("User is already a member of this organization");
        }
        
        OrganizationMember membership = new OrganizationMember();
        membership.setUser(newMember);
        membership.setOrganization(organization);
        membership.setRole(role);
        
        OrganizationMember savedMembership = organizationMemberRepository.save(membership);
        
        return new OrganizationMemberDto(
                savedMembership.getId(),
                savedMembership.getUser().getId(),
                savedMembership.getUser().getName(),
                savedMembership.getUser().getEmail(),
                savedMembership.getRole(),
                savedMembership.getJoinedAt()
        );
    }

    @Transactional
    public void removeMember(UUID organizationId, UUID memberId) {
        User currentUser = getCurrentUser();
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new NotFoundException("Organization not found"));
        
        // Check if current user is admin
        OrganizationMember currentMembership = organizationMemberRepository
                .findByUserAndOrganization(currentUser, organization)
                .orElseThrow(() -> new NotFoundException("You are not a member of this organization"));
        
        if (!"ADMIN".equals(currentMembership.getRole())) {
            throw new RuntimeException("Only admins can remove members");
        }
        
        OrganizationMember memberToRemove = organizationMemberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("Member not found"));
        
        if (!memberToRemove.getOrganization().getId().equals(organizationId)) {
            throw new RuntimeException("Member does not belong to this organization");
        }
        
        organizationMemberRepository.delete(memberToRemove);
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}