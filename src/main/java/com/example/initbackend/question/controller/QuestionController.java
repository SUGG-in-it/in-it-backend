package com.example.initbackend.question.controller;

import com.example.initbackend.global.response.StatusEnum;
import com.example.initbackend.global.response.SuccessResponse;
import com.example.initbackend.question.dto.UpdateQuestionRequestDto;
import com.example.initbackend.question.service.QuestionService;
import com.example.initbackend.question.vo.IssueQuestionIdResponseVo;
import lombok.Getter;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Getter
@RestController
@RequestMapping("api/questions")
public class QuestionController {

    private final QuestionService questionService;
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping({ "" })
    public SuccessResponse issueQuestionId() {
        // token 까서 userid 넘겨주기
        IssueQuestionIdResponseVo issueQuestionIdResponse = questionService.issueQuestionId(Long.valueOf(1));

        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("Issued QuestionId")
                .data(issueQuestionIdResponse)
                .build();

        return res;
    }
    @PutMapping({ "/{questionId}" })
    public SuccessResponse updateQuestion(@PathVariable("questionId") Long questionId, @Valid @RequestBody final UpdateQuestionRequestDto requestDto) {
        questionService.UpdateQuestion(questionId, requestDto);

        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("Modified Question")
                .build();

        return res;
    }
}
