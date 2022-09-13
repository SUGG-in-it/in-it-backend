package com.example.initbackend.answer.controller;


import com.example.initbackend.answer.domain.Answer;
import com.example.initbackend.answer.dto.CreateAnswerRequestDto;
import com.example.initbackend.answer.dto.GetAnswerRequestDto;
import com.example.initbackend.answer.service.AnswerService;
import com.example.initbackend.answer.vo.GetAnswerResponseVo;
import com.example.initbackend.global.response.StatusEnum;
import com.example.initbackend.global.response.SuccessResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.data.domain.Pageable;

import java.util.List;

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

    @GetMapping
    public SuccessResponse getAnswer(HttpServletRequest request, Pageable pageable){
        GetAnswerResponseVo getAnswerResponseVo = answerService.getAnswer(request, pageable);

        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .data(getAnswerResponseVo)
                .build();
        return res;
    }
}
