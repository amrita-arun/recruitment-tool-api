package com.example.recruitment_svc.repository;

import com.example.recruitment_svc.model.Organization;
import com.example.recruitment_svc.model.OrganizationMember;
import com.example.recruitment_svc.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrganizationMemberRepository extends JpaRepository<OrganizationMember, UUID> {
    List<OrganizationMember> findByUserId(UUID userId);
    List<OrganizationMember> findByOrganizationId(UUID organizationId);
    Optional<OrganizationMember> findByUserIdAndOrganizationId(UUID userId, UUID organizationId);
    boolean existsByUserIdAndOrganizationId(UUID userId, UUID organizationId);
    
    // Additional methods for multi-tenant support
    List<OrganizationMember> findByUser(User user);
    List<OrganizationMember> findByOrganization(Organization organization);
    Optional<OrganizationMember> findByUserAndOrganization(User user, Organization organization);
}