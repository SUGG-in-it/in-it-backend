package com.example.initbackend.answer.controller;


import com.example.initbackend.answer.dto.UpdateAnswerRequestDto;
import com.example.initbackend.answer.service.AnswerService;
import com.example.initbackend.answer.vo.GetAnswerResponseVo;
import com.example.initbackend.answer.vo.IssueAnswerIdResponseVo;
import com.example.initbackend.global.response.StatusEnum;
import com.example.initbackend.global.response.SuccessResponse;
import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.data.domain.Pageable;


@RestController
@AllArgsConstructor
@RequestMapping("api/answers")
public class AnswerController {

    private final AnswerService answerService;

    @PostMapping
    public SuccessResponse issueAnswerId(HttpServletRequest request) {
        // token 까서 userid 넘겨주기
        IssueAnswerIdResponseVo issueAnswerIdResponse = answerService.issueAnswerId(request);

        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("Issued answerId")
                .data(issueAnswerIdResponse)
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

    @PutMapping
    public SuccessResponse updateAnswer(HttpServletRequest request,  @Valid @RequestBody UpdateAnswerRequestDto requestDto){
        answerService.updateAnswer(request, requestDto);

        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .build();
        return res;
    }
}
