package com.example.recruitment_svc.repository;

import com.example.recruitment_svc.model.Organization;
import com.example.recruitment_svc.model.RecruitmentCycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RecruitmentCycleRepository extends JpaRepository<RecruitmentCycle, UUID> {
    List<RecruitmentCycle> findByOrganizationId(UUID organizationId);
    List<RecruitmentCycle> findByOrganizationIdAndStatus(UUID organizationId, RecruitmentCycle.RecruitmentCycleStatus status);
    
    // Additional methods for multi-tenant support
    List<RecruitmentCycle> findByOrganization(Organization organization);
}