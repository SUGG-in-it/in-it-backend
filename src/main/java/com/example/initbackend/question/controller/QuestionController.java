package com.example.initbackend.question.controller;

import com.example.initbackend.answer.vo.GetAnswerResponseVo;
import com.example.initbackend.comment.vo.GetCommentsResponseVo;
import com.example.initbackend.global.response.StatusEnum;
import com.example.initbackend.global.response.SuccessResponse;
import com.example.initbackend.question.dto.UpdateQuestionRequestDto;
import com.example.initbackend.question.service.QuestionService;
import com.example.initbackend.question.vo.*;
import lombok.Getter;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    public SuccessResponse issueQuestionId(HttpServletRequest request) {
        IssueQuestionIdResponseVo issueQuestionIdResponseVo = questionService.issueQuestionId(request);

        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("Issued QuestionId")
                .data(issueQuestionIdResponseVo)
                .build();

        return res;
    }
    @PutMapping({ "/{questionId}" })
    public SuccessResponse updateQuestion(HttpServletRequest request, @PathVariable("questionId") Long questionId, @Valid @RequestBody final UpdateQuestionRequestDto requestDto) {
        questionService.UpdateQuestion(request, questionId, requestDto);

        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("Modified Question")
                .build();

        return res;
    }

    @GetMapping({ "/{questionId}" })
    public SuccessResponse getQuestion(HttpServletRequest request, @PathVariable("questionId") Long questionId) {
        GetQuestionResponseVo getQuestionResponseVo = questionService.GetQuestion(request, questionId);

        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("Get Question")
                .data(getQuestionResponseVo)
                .build();

        return res;
    }

    @GetMapping({ "" })
    public SuccessResponse getQuestions(HttpServletRequest request, Pageable pageable, @RequestParam("type") String type) {
        GetQuestionsResponseVo getQuestionsResponse = questionService.GetQuestions(request, pageable, type);

        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("Get Question")
                .data(getQuestionsResponse)
                .build();

        return res;
    }

    @DeleteMapping({ "/{questionId}" })
    public SuccessResponse deleteQuestion(HttpServletRequest request, @PathVariable("questionId") Long questionId) {
        questionService.DeleteQuestion(request, questionId);

        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("Question Deleted")
                .build();

        return res;
    }
    @GetMapping({ "/main" })
    public SuccessResponse getBannerQuestion(@RequestParam("type") String type) {
        GetBannerQuestionIdResponseVo getBannerQuestionIdResponseVo = questionService.GetBannerQuestionId(type);

        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("Get Question")
                .data(getBannerQuestionIdResponseVo)
                .build();

        return res;
    }

    @GetMapping({ "/page" })
    public SuccessResponse getQuestionsTotalPageNum(Pageable pageable, @RequestParam("type") String type) {
        GetQuestionsTotalPageNumResponseVo getQuestionsTotalPageNumResponseVo = questionService.GetQuestionsTotalPageNum(pageable, type);

        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("Get Questions Total Page Number")
                .data(getQuestionsTotalPageNumResponseVo)
                .build();

        return res;
    }

    @GetMapping({ "/user-page" })
    public SuccessResponse getUserQuestionsTotalPageNum(HttpServletRequest request, Pageable pageable) {
        GetUserQuestionsTotalPageNumResponseVo getUserQuestionsTotalPageNumVo = questionService.GetUserQuestionsTotalPageNum(request, pageable);

        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("Get User Questions Total Page Number")
                .data(getUserQuestionsTotalPageNumVo)
                .build();

        return res;
    }

    @GetMapping({"/manage"})
    public SuccessResponse getManagedQuestions(HttpServletRequest servletRequest, Pageable pageable) {
        GetQuestionsResponseVo getQuestionsResponseVo = questionService.getManagedQuestions(servletRequest, pageable);

        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("Get Managed Questions")
                .data(getQuestionsResponseVo)
                .build();

        return res;
    }

    @GetMapping({"/search"})
    public SuccessResponse searchQuestions(@RequestParam(required = false) String query, @RequestParam String type, Pageable pageable, @RequestParam(required = false) String tag) {
        SearchQuestionsResponseVo searchQuestionsResponseVo = questionService.searchQuestions(query, type, pageable, tag);

        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("Search Successfully")
                .data(searchQuestionsResponseVo)
                .build();

        return res;
    }

    @GetMapping({ "/search/page"})
    public SuccessResponse getSearchQuestionsTotalPageNum(@RequestParam(required = false) String query, @RequestParam String type, Pageable pageable, @RequestParam(required = false) String tag) {
        GetQuestionsTotalPageNumResponseVo getQuestionsTotalPageNumResponseVo = questionService.GetSearchQuestionsTotalPageNum(query, type, pageable, tag);

        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("Get Search Question Total Page Number")
                .data(getQuestionsTotalPageNumResponseVo)
                .build();

        return res;
    }



}
