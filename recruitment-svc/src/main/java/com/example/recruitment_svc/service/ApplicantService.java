package com.example.recruitment_svc.service;

import com.example.recruitment_svc.dtos.ApplicantDetailDto;
import com.example.recruitment_svc.dtos.ApplicantSummaryDto;
import com.example.recruitment_svc.mappers.ApplicantMapper;
import com.example.recruitment_svc.repository.ApplicantRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.io.InputStream;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ApplicantService {
    private final ApplicantRepository repo;
    private final ApplicantMapper map;

    public Page<ApplicantSummaryDto> list(String q, Pageable p) {
        return repo.search((q == null || q.isBlank()) ? null : q, p).map(map :: toSummary);
    }

    public ApplicantDetailDto get(UUID id) {
        var a = repo.findById(id).orElseThrow(() -> new ChangeSetPersister.NotFoundException("applicant " + id));
        return map.toDetail(a);
    }

    @Transactional
    public int importCSV(InputStream csv) {
        return -1;
    }

}
