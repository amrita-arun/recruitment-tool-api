package com.example.recruitment_svc.service;

import com.example.recruitment_svc.dtos.CommentDto;
import com.example.recruitment_svc.dtos.CreateCommentRequest;
import com.example.recruitment_svc.errors.NotFoundException;
import com.example.recruitment_svc.mappers.CommentMapper;
import com.example.recruitment_svc.model.ApplicantComment;
import com.example.recruitment_svc.repository.ApplicantCommentRepository;
import com.example.recruitment_svc.repository.ApplicantRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import javax.xml.stream.events.Comment;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApplicantCommentService {
    private final ApplicantRepository applicants;
    private final ApplicantCommentRepository comments;
    private final CommentMapper mapper;

    @Transactional(readOnly = true)
    public Page<CommentDto> list(UUID applicantId, Pageable p) {
        if (!applicants.existsById(applicantId)) {
            throw new NotFoundException("Applicant " + applicantId + " not found");
        }
        return comments.findByApplicantIdOrderByCreatedAtDesc(applicantId, p).map(mapper::toDto);
    }

    public CommentDto create(UUID applicantId, CreateCommentRequest req) {
        var applicant = applicants.findById(applicantId).orElseThrow(() -> new NotFoundException("Applicant " + applicantId + " not found"));

        var comment = new ApplicantComment();
        comment.setApplicant(applicant);
        comment.setAuthor(req.author());
        comment.setBody(req.body());

        var saved = comments.save(comment);

        return mapper.toDto(saved);

    }
}
