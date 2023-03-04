package com.example.initbackend.answer.controller;


import com.example.initbackend.answer.dto.IssueAnswerIdDto;
import com.example.initbackend.answer.dto.UpdateAnswerRequestDto;
import com.example.initbackend.answer.service.AnswerService;
import com.example.initbackend.answer.vo.*;
import com.example.initbackend.comment.vo.GetCommentsResponseVo;
import com.example.initbackend.global.jwt.JwtTokenProvider;
import com.example.initbackend.global.jwt.JwtUtil;
import com.example.initbackend.global.response.StatusEnum;
import com.example.initbackend.global.response.SuccessResponse;
import lombok.AllArgsConstructor;

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

    private final JwtUtil jwtUtil;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping
    public SuccessResponse issueAnswerId(HttpServletRequest request, @RequestBody IssueAnswerIdDto requestDto) {

        IssueAnswerIdResponseVo issueAnswerIdResponseVo = answerService.issueAnswerId(request, requestDto);

        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("Issued answerId")
                .data(issueAnswerIdResponseVo)
                .build();

        return res;
    }

    @GetMapping
    public SuccessResponse getAnswer(Pageable pageable, @RequestParam Long questionId){
        List<GetAnswerResponseVo> getAnswerResponseVo = answerService.getAnswer(pageable, questionId);

        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("Get Answer")
                .data(getAnswerResponseVo)
                .build();
        return res;
    }

    @PutMapping({ "/{answerId}" })
    public SuccessResponse updateAnswer(HttpServletRequest request, @Valid @RequestBody UpdateAnswerRequestDto requestDto, @PathVariable("answerId") Long answerId){
        answerService.updateAnswer(getUserIdFromToken(request), requestDto, answerId);

        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("Update Answer Success")
                .build();
        return res;
    }

    @DeleteMapping({ "/{answerId}" })
    public SuccessResponse deleteAnswer(HttpServletRequest request, @PathVariable("answerId") Long answerId){
        answerService.deleteAnswer(getUserIdFromToken(request), answerId);

        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("Delete Answer Success")
                .build();
        return res;
    }

    @PostMapping("/select/{answerId}")
    public SuccessResponse selectAnswer(HttpServletRequest request, @PathVariable("answerId") Long answerId) {

        answerService.selectAnswer(getUserIdFromToken(request), answerId);

        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("Answer is selected Successfully")
                .build();

        return res;
    }

    @GetMapping({"/page"})
    public SuccessResponse getAnswersTotalPageNum(Pageable pageable, @RequestParam Long questionId) {
        GetAnswersTotalPageNumResponseVo getAnswersTotalPageNumResponseVo = answerService.getAnswersTotalPageNum(pageable, questionId);

        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("Get answers total page number")
                .data(getAnswersTotalPageNumResponseVo)
                .build();

        return res;
    }

    @GetMapping({"/user-page"})
    public SuccessResponse getUserAnswersTotalPageNum(HttpServletRequest request, Pageable pageable) {
        GetUserAnswersTotalPageNumResponseVo getUserAnswersTotalPageNumResponse = answerService.getUserAnswersTotalPageNum(getUserIdFromToken(request), pageable);

        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("Get user answers total page number")
                .data(getUserAnswersTotalPageNumResponse)
                .build();

        return res;
    }

    @GetMapping({"/manage"})
    public SuccessResponse getManagedAnswers(HttpServletRequest request, Pageable pageable) {
        GetManagedAnswersResponseVo getAnswerResponseVo = answerService.getManagedAnswers(getUserIdFromToken(request), pageable);

        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("Get my answers")
                .data(getAnswerResponseVo)
                .build();

        return res;
    }

    private Long getUserIdFromToken(HttpServletRequest request){
        String token = jwtTokenProvider.resolveAccessToken(request);
        return jwtUtil.getPayloadByToken(token);
    }
}
