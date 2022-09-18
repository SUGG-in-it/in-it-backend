package com.example.initbackend.comment.controller;

import com.example.initbackend.comment.dto.RegisterCommentRequestDto;
import com.example.initbackend.comment.service.CommentService;
import com.example.initbackend.comment.vo.GetCommentsResponseVo;
import com.example.initbackend.comment.vo.GetCommentsTotalPageNumResponseVo;
import com.example.initbackend.global.response.StatusEnum;
import com.example.initbackend.global.response.SuccessResponse;
import lombok.Getter;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

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
        commentService.registerComment(requestDto);

        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("comment successfully registered")
                .build();

        return res;
    }

    @GetMapping
    public SuccessResponse getComments(Pageable pageable,@Valid @RequestParam final Long answerId) {
        GetCommentsResponseVo getCommentsResponse = commentService.getComments(pageable, answerId);

        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("get comments")
                .data(getCommentsResponse)
                .build();

        return res;
    }

    @DeleteMapping({"/{commentId}"})
    public SuccessResponse deleteComment(@PathVariable("commentId") Long commentId) {
        commentService.deleteComment(commentId);

        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("comment successfully deleted")
                .build();

        return res;
    }

    @GetMapping({"/page"})
    public SuccessResponse getCommentsTotalPageNum(Pageable pageable,@Valid @RequestParam final Long answerId) {
        GetCommentsTotalPageNumResponseVo getCommentsTotalPageNumResponse = commentService.getCommentsTotalPageNum(pageable, answerId);

        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("get comments total page number")
                .data(getCommentsTotalPageNumResponse)
                .build();

        return res;
    }
}