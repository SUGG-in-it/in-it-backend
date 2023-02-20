package com.example.initbackend.likes.controller;

import com.example.initbackend.global.response.StatusEnum;
import com.example.initbackend.global.response.SuccessResponse;
import com.example.initbackend.likes.service.LikesService;
import com.example.initbackend.question.vo.IssueQuestionIdResponseVo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@AllArgsConstructor
@RequestMapping("api/like")
public class LikesController {

    private LikesService likesService;

    @PostMapping({ "/{questionId}" })
    public SuccessResponse addLikes(HttpServletRequest request, @PathVariable("questionId") Long questionId) {
        likesService.addLikes(request, questionId);

        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("Likes + 1")
                .build();

        return res;
    }
}
