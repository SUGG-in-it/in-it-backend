package com.example.initbackend.question.controller;

import com.example.initbackend.global.response.StatusEnum;
import com.example.initbackend.global.response.SuccessResponse;
import com.example.initbackend.question.service.QuestionService;
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
        Integer questionId = questionService.issueQuestionId(12);

        // 여기 왜 안되는건지 모르겠다잉...?
        class IssueQuestionIdResponse{
            private int questionId;
            public IssueQuestionIdResponse(Integer questionId) {
                this.questionId = questionId;
            }
        }
        IssueQuestionIdResponse issueQuestionIdResponse = new IssueQuestionIdResponse(questionId);


        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("Issued QuestionId")
                .data(String.valueOf(questionId))
                .build();

        return res;
    }
}
