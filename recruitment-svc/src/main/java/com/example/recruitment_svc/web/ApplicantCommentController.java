package com.example.recruitment_svc.web;

import com.example.recruitment_svc.dtos.CommentDto;
import com.example.recruitment_svc.dtos.CreateCommentRequest;
import com.example.recruitment_svc.service.ApplicantCommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.xml.stream.events.Comment;
import java.util.UUID;

@RestController
@RequestMapping("/api/applicants/{id}/comments")
@RequiredArgsConstructor
public class ApplicantCommentController {
    private final ApplicantCommentService svc;

    @GetMapping
    public Page<CommentDto> list(
            @PathVariable UUID id,
            @org.springframework.data.web.PageableDefault(size = 20, sort="createdAt", direction=org.springframework.data.domain.Sort.Direction.DESC)
            org.springframework.data.domain.Pageable pageable
    ) {
        return svc.list(id, pageable);
    }

    @PostMapping
    public CommentDto create(@PathVariable UUID id, @Valid @RequestBody CreateCommentRequest req) {
        return svc.create(id, req);
    }

}
