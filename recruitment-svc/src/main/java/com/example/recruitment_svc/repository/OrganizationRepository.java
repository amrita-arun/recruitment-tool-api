package com.example.recruitment_svc.repository;

import com.example.recruitment_svc.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, UUID> {
    List<Organization> findByNameContainingIgnoreCase(String name);
}