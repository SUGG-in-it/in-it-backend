package com.example.initbackend.comment.controller;

import com.example.initbackend.comment.dto.RegisterCommentRequestDto;
import com.example.initbackend.comment.service.CommentService;
import com.example.initbackend.comment.vo.GetCommentsResponseVo;
import com.example.initbackend.comment.vo.GetCommentsTotalPageNumResponseVo;
import com.example.initbackend.comment.vo.GetUserCommentsTotalPageNumResponseVo;
import com.example.initbackend.global.response.StatusEnum;
import com.example.initbackend.global.response.SuccessResponse;
import lombok.Getter;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    public SuccessResponse registerComment(HttpServletRequest servletRequest, @Valid @RequestBody final RegisterCommentRequestDto requestDto) {
        commentService.registerComment(servletRequest, requestDto);

        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("Comment Registered")
                .build();

        return res;
    }

    @GetMapping
    public SuccessResponse getComments(Pageable pageable, @Valid @RequestParam final Long answerId) {
        GetCommentsResponseVo getCommentsResponseVo = commentService.getComments(pageable, answerId);

        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("Get Comments")
                .data(getCommentsResponseVo)
                .build();

        return res;
    }

    @DeleteMapping({"/{commentId}"})
    public SuccessResponse deleteComment(HttpServletRequest servletRequest, @PathVariable("commentId") Long commentId) {
        commentService.deleteComment(servletRequest, commentId);

        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("Comment Deleted")
                .build();

        return res;
    }

    @GetMapping({"/page"})
    public SuccessResponse getCommentsTotalPageNum(Pageable pageable, @Valid @RequestParam final Long answerId) {
        GetCommentsTotalPageNumResponseVo getCommentsTotalPageNumResponseVo = commentService.getCommentsTotalPageNum(pageable, answerId);

        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("Get Comments Total Page Number")
                .data(getCommentsTotalPageNumResponseVo)
                .build();

        return res;
    }

    @GetMapping({"/user-page"})
    public SuccessResponse getUserCommentsTotalPageNum(HttpServletRequest request, Pageable pageable) {
        GetUserCommentsTotalPageNumResponseVo getUserCommentsTotalPageNumResponseVo = commentService.getUserCommentsTotalPageNum(request, pageable);

        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("Get User Comments Total Page Number")
                .data(getUserCommentsTotalPageNumResponseVo)
                .build();

        return res;
    }

    @GetMapping({"/manage"})
    public SuccessResponse getManagedComments(HttpServletRequest servletRequest, Pageable pageable) {
        GetCommentsResponseVo getCommentsResponseVo = commentService.getManagedComments(servletRequest, pageable);

        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("Get Managed Comments")
                .data(getCommentsResponseVo)
                .build();

        return res;
    }
}