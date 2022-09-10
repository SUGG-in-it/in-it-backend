package com.example.initbackend.comment.controller;

import com.example.initbackend.comment.dto.RegisterCommentRequestDto;
import com.example.initbackend.comment.service.CommentService;
import com.example.initbackend.global.response.StatusEnum;
import com.example.initbackend.global.response.SuccessResponse;
import lombok.Getter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Getter
@RestController
@RequestMapping("api/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public SuccessResponse registerComment(@Valid @RequestBody final RegisterCommentRequestDto requestDto) {

        System.out.println("111111111111111111111");
        commentService.registerComment(requestDto);

        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("comment successfully registered")
                .build();

        return res;
    }
}