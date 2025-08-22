package com.example.recruitment_svc.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "applicant_comments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ApplicantComment {
    @Id
    @EqualsAndHashCode.Include
    @org.hibernate.annotations.UuidGenerator
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "applicant_id", nullable = false)
    @JsonIgnore
    private Applicant applicant;

    @NotBlank
    private String author;

    @NotBlank
    private String body;

    @Column(name = "created_at", updatable = false, insertable = false)
    private OffsetDateTime createdAt;

    public ApplicantComment(Applicant applicant, String author, String body) {
        this.applicant = applicant;
        this.author = author;
        this.body = body;
    }

}
