package com.example.initbackend.answer.controller;


import com.example.initbackend.answer.dto.IssueAnswerIdDto;
import com.example.initbackend.answer.dto.UpdateAnswerRequestDto;
import com.example.initbackend.answer.service.AnswerService;
import com.example.initbackend.answer.vo.GetAnswerResponseVo;
import com.example.initbackend.answer.vo.GetAnswersTotalPageNumResponseVo;
import com.example.initbackend.answer.vo.IssueAnswerIdResponseVo;
import com.example.initbackend.comment.vo.GetCommentsResponseVo;
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
    public SuccessResponse issueAnswerId(HttpServletRequest request, @RequestBody IssueAnswerIdDto requestDto) {
        IssueAnswerIdResponseVo issueAnswerIdResponse = answerService.issueAnswerId(request, requestDto);

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
    public SuccessResponse updateAnswer(HttpServletRequest request, @Valid @RequestBody UpdateAnswerRequestDto requestDto, @PathVariable("answerId") Long answerId){
        answerService.updateAnswer(request, requestDto, answerId);

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

    @GetMapping({"/page"})
    public SuccessResponse getAnswersTotalPageNum(Pageable pageable, @RequestParam Long questionId) {
        GetAnswersTotalPageNumResponseVo getAnswersTotalPageNumResponse = answerService.getAnswersTotalPageNum(pageable, questionId);

        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("get answers total page number")
                .data(getAnswersTotalPageNumResponse)
                .build();

        return res;
    }

    @GetMapping({"/manage"})
    public SuccessResponse getManagedAnswers(HttpServletRequest servletRequest, Pageable pageable) {
        GetAnswerResponseVo getAnswerResponse = answerService.getManagedAnswers(servletRequest, pageable);

        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("get my answers")
                .data(getAnswerResponse)
                .build();

        return res;
    }
}
