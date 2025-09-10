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
      where (:q is null
             or lower(a.name)  like lower(concat('%', cast(:q as string), '%'))
             or lower(a.email) like lower(concat('%', cast(:q as string), '%')))
      order by a.createdAt desc
    """)
    Page<Applicant> search(@Param("q") String q, Pageable pageable);

    @Query("""
      select a from Applicant a
      where (:q is null
             or lower(a.name)  like lower(concat('%', cast(:q as string), '%'))
             or lower(a.email) like lower(concat('%', cast(:q as string), '%')))
        and (:status is null or a.status = :status)
      order by a.createdAt desc
    """)
    Page<Applicant> search(
            @Param("q") String q,
            @Param("status") com.example.recruitment_svc.model.Status status,
            org.springframework.data.domain.Pageable pageable
    );

}
