package com.example.recruitment_svc.mappers;

import com.example.recruitment_svc.dtos.CommentDto;
import com.example.recruitment_svc.model.ApplicantComment;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {
    public CommentDto toDto (ApplicantComment c) {
        return new CommentDto(c.getId(), c.getApplicant().getId(), c.getApplicant().getName(), c.getBody(), c.getCreatedAt());
    }
}
