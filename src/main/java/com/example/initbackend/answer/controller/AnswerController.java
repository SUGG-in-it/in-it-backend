package com.example.initbackend.answer.controller;


import com.example.initbackend.answer.dto.SelectAnswerRequestDto;
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
        IssueAnswerIdResponseVo issueAnswerIdResponse = answerService.issueAnswerId(request);

        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("Issued answerId")
                .data(issueAnswerIdResponse)
                .build();

        return res;
    }

    @GetMapping
    public SuccessResponse getAnswer(Pageable pageable, @RequestParam Long questionId){
        GetAnswerResponseVo getAnswerResponseVo = answerService.getAnswer(pageable, questionId);

        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .data(getAnswerResponseVo)
                .build();
        return res;
    }

    @PutMapping({ "/{answerId}" })
    public SuccessResponse updateAnswer( @Valid @RequestBody UpdateAnswerRequestDto requestDto, @PathVariable("answerId") Long answerId){
        answerService.updateAnswer(requestDto, answerId);

        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("Update Answer Success")
                .build();
        return res;
    }

    @DeleteMapping({ "/{answerId}" })
    public SuccessResponse deleteAnswer(@PathVariable("answerId") Long answerId){
        answerService.deleteAnswer(answerId);

        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("Delete Answer Success")
                .build();
        return res;
    }

    @PostMapping("/select/{answerId}")
    public SuccessResponse selectAnswer(@PathVariable("answerId") Long answerId) {

        answerService.selectAnswer(answerId);

        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("Answer is selected Successfully")
                .build();

        return res;
    }
}
