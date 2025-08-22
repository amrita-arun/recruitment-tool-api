package com.example.recruitment_svc.repository;

import com.example.recruitment_svc.model.ApplicantComment;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ApplicantCommentRepository extends JpaRepository<ApplicantComment, UUID> {

    @EntityGraph(attributePaths = "applicant")
    Page<ApplicantComment> findByApplicantIdOrderByCreatedAtDesc(UUID applicantId, Pageable pageable);
}
