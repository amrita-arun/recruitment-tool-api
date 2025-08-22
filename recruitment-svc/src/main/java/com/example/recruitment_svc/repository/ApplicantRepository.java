package com.example.recruitment_svc.repository;

import com.example.recruitment_svc.model.Applicant;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.UUID;

public interface ApplicantRepository extends JpaRepository<Applicant, UUID> {
    @Query("""
            select a from Applicant a
            where (:q is null or lower(a.name) like lower(concat('%', :q, '%'))
            or lower(a.email) like lower(concat('%', :q, '%')))
            """)
    Page<Applicant> search(@Param("q") String q, Pageable pageable);
}
