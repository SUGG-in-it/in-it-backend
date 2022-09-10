package com.example.initbackend.question.controller;

import com.example.initbackend.global.response.StatusEnum;
import com.example.initbackend.global.response.SuccessResponse;
import com.example.initbackend.question.service.QuestionService;
import com.example.initbackend.question.vo.IssueQuestionIdResponseVo;
import lombok.Getter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
        IssueQuestionIdResponseVo issueQuestionIdResponse = questionService.issueQuestionId(1);

        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("Issued QuestionId")
                .data(issueQuestionIdResponse)
                .build();

        return res;
    }

    @PostMapping({"/image"})
    // dto 로 수정
    public SuccessResponse uploadImage(@RequestParam("id") String category, @RequestPart(value = "image") MultipartFile multipartFile) {
        questionService.uploadImage(category, multipartFile);
        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("Uploaded Image")
                .build();

        return res;
    }
}
