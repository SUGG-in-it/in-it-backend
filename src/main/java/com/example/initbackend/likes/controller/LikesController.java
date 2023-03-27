package com.example.initbackend.likes.controller;

import com.example.initbackend.global.response.StatusEnum;
import com.example.initbackend.global.response.SuccessResponse;
import com.example.initbackend.likes.service.LikesService;
import com.example.initbackend.likes.vo.GetLikeResponseVo;
import com.example.initbackend.question.vo.IssueQuestionIdResponseVo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@AllArgsConstructor
@RequestMapping("api/like")
public class LikesController {

    private LikesService likesService;

    @PostMapping({ "/{questionId}/add" })
    public SuccessResponse addLike(HttpServletRequest request, @PathVariable("questionId") Long questionId) {
        likesService.addLike(request, questionId);

        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("Add Like")
                .build();

        return res;
    }

    @PostMapping({ "/{questionId}/cancel" })
    public SuccessResponse cancelLike(HttpServletRequest request, @PathVariable("questionId") Long questionId) {
        likesService.cancelLike(request, questionId);

        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("Cancel Like")
                .build();

        return res;
    }

    @GetMapping({ "/{questionId}" })
    public SuccessResponse getLike(HttpServletRequest request, @PathVariable("questionId") Long questionId) {
        GetLikeResponseVo getLikeResponseVo = likesService.getLike(request, questionId);

        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("Get Like")
                .data(getLikeResponseVo)
                .build();

        return res;
    }
}
