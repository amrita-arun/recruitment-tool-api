package com.example.recruitment_svc.repository;

import com.example.recruitment_svc.model.Applicant;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ApplicantRepository extends JpaRepository<Applicant, UUID> {
    
    @Query("""
      select a from Applicant a
      join a.recruitmentCycle rc
      join rc.organization o
      where o.id = :organizationId
        and (:q is null
             or lower(a.name)  like lower(concat('%', cast(:q as string), '%'))
             or lower(a.email) like lower(concat('%', cast(:q as string), '%')))
      order by a.createdAt desc
    """)
    Page<Applicant> searchByOrganization(
            @Param("organizationId") UUID organizationId,
            @Param("q") String q, 
            Pageable pageable
    );

    @Query("""
      select a from Applicant a
      join a.recruitmentCycle rc
      join rc.organization o
      where o.id = :organizationId
        and (:q is null
             or lower(a.name)  like lower(concat('%', cast(:q as string), '%'))
             or lower(a.email) like lower(concat('%', cast(:q as string), '%')))
        and (:status is null or a.status = :status)
      order by a.createdAt desc
    """)
    Page<Applicant> searchByOrganization(
            @Param("organizationId") UUID organizationId,
            @Param("q") String q,
            @Param("status") com.example.recruitment_svc.model.Status status,
            Pageable pageable
    );

    @Query("""
      select a from Applicant a
      join a.recruitmentCycle rc
      where rc.id = :recruitmentCycleId
        and (:q is null
             or lower(a.name)  like lower(concat('%', cast(:q as string), '%'))
             or lower(a.email) like lower(concat('%', cast(:q as string), '%')))
      order by a.createdAt desc
    """)
    Page<Applicant> searchByRecruitmentCycle(
            @Param("recruitmentCycleId") UUID recruitmentCycleId,
            @Param("q") String q, 
            Pageable pageable
    );

    @Query("""
      select a from Applicant a
      join a.recruitmentCycle rc
      where rc.id = :recruitmentCycleId
        and (:q is null
             or lower(a.name)  like lower(concat('%', cast(:q as string), '%'))
             or lower(a.email) like lower(concat('%', cast(:q as string), '%')))
        and (:status is null or a.status = :status)
      order by a.createdAt desc
    """)
    Page<Applicant> searchByRecruitmentCycle(
            @Param("recruitmentCycleId") UUID recruitmentCycleId,
            @Param("q") String q,
            @Param("status") com.example.recruitment_svc.model.Status status,
            Pageable pageable
    );

    @Query("""
      select a from Applicant a
      join a.recruitmentCycle rc
      join rc.organization o
      where o.id = :organizationId
        and a.id = :applicantId
    """)
    java.util.Optional<Applicant> findByIdAndOrganization(
            @Param("applicantId") UUID applicantId,
            @Param("organizationId") UUID organizationId
    );

    @Query("""
      select a from Applicant a
      join a.recruitmentCycle rc
      where rc.id = :recruitmentCycleId
        and a.id = :applicantId
    """)
    java.util.Optional<Applicant> findByIdAndRecruitmentCycle(
            @Param("applicantId") UUID applicantId,
            @Param("recruitmentCycleId") UUID recruitmentCycleId
    );
}
