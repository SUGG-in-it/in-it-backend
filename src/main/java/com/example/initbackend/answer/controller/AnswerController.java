package com.example.initbackend.answer.controller;


import com.example.initbackend.answer.dto.CreateAnswerRequestDto;
import com.example.initbackend.answer.service.AnswerService;
import com.example.initbackend.global.response.StatusEnum;
import com.example.initbackend.global.response.SuccessResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Component
@RestController
@AllArgsConstructor
@RequestMapping("api/answers")
public class AnswerController {

    private final AnswerService answerService;

    // 확인 아직 안됨.
    @PostMapping
    public SuccessResponse createAnswer(HttpServletRequest request,  @Valid @RequestBody CreateAnswerRequestDto requestDto){
        answerService.createAnswer(request, requestDto);

        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .build();
        return res;
    }
}
