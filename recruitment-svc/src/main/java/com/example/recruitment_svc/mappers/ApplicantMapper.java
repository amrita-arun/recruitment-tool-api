package com.example.recruitment_svc.mappers;

import com.example.recruitment_svc.dtos.ApplicantDetailDto;
import com.example.recruitment_svc.dtos.ApplicantSummaryDto;
import com.example.recruitment_svc.model.Applicant;
import org.springframework.stereotype.Component;

// Entity to DTO

@Component
public class ApplicantMapper {
    public ApplicantSummaryDto toSummary(Applicant a) {
        return new ApplicantSummaryDto(a.getId(), a.getName(), a.getEmail(), a.getMajor(), a.getYear(), a.getGpa(), a.getStatus(), a.getCreatedAt());
    }

    public ApplicantDetailDto toDetail(Applicant a) {
        return new ApplicantDetailDto(a.getId(), a.getName(), a.getEmail(), a.getPhone(), a.getLocation(), a.getMajor(), a.getYear(), a.getGpa(), a.getStatus(), a.getRaw(), a.getCreatedAt(), a.getResumeUrl(), a.getAnswers());

    }
}
