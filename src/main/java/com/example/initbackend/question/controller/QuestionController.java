package com.example.initbackend.question.controller;

import com.example.initbackend.global.response.StatusEnum;
import com.example.initbackend.global.response.SuccessResponse;
import com.example.initbackend.question.service.QuestionService;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        Long questionId = questionService.issueQuestionId(12);

        class IssueQuestionIdResponse{
            @JsonProperty
            private Long questionId;
            public IssueQuestionIdResponse(Long questionId) {
                this.questionId = questionId;
            }
        }

        IssueQuestionIdResponse issueQuestionIdResponse = new IssueQuestionIdResponse(questionId);

        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("Issued QuestionId")
                .data(issueQuestionIdResponse)
                .build();

        return res;
    }
}
