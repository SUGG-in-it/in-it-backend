package com.example.initbackend.answer.service;

import com.example.initbackend.answer.domain.Answer;
import com.example.initbackend.answer.dto.CreateAnswerRequestDto;
import com.example.initbackend.answer.dto.GetAnswerRequestDto;
import com.example.initbackend.answer.repository.AnswerRepository;
import com.example.initbackend.answer.vo.GetAnswerResponseVo;
import com.example.initbackend.global.handler.CustomException;
import com.example.initbackend.global.jwt.JwtTokenProvider;
import com.example.initbackend.global.jwt.JwtUtil;
import com.example.initbackend.global.response.ErrorCode;
import com.example.initbackend.question.controller.QuestionController;
import com.example.initbackend.question.domain.Question;
import com.example.initbackend.question.repository.QuestionRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Slf4j
@Getter
@Service
@RequiredArgsConstructor
public class AnswerService {

    private final JwtUtil jwtUtil;
    private final JwtTokenProvider jwtTokenProvider;
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    public void createAnswer(HttpServletRequest request, CreateAnswerRequestDto createAnswerRequestDto){

        String token = jwtTokenProvider.resolveAccessToken(request);
        Long userId  = jwtUtil.getPayloadByToken(token);
        Long questionId = createAnswerRequestDto.getQuestionId();
        if (isDuplicatedAnswer(userId, questionId)){
            throw new CustomException(ErrorCode.CONFLICT);
        }

        Optional<Question> optionalQuestion = questionRepository.findById(questionId);
        if(optionalQuestion.isPresent()){
            Answer answer = createAnswerRequestDto.toEntity();
            answerRepository.save(answer);
        }else{
            throw new CustomException(ErrorCode.DATA_NOT_FOUND);
        }
    }

    public GetAnswerResponseVo getAnswer(HttpServletRequest request, Pageable pageable){

        String token = jwtTokenProvider.resolveAccessToken(request);
        Long userId  = jwtUtil.getPayloadByToken(token);

        Page<Answer> optionalAnswer = answerRepository.findAllByQuestionIdOrderByCreateDateDesc(userId, pageable);
        GetAnswerResponseVo answerList = new GetAnswerResponseVo(optionalAnswer.getContent());
        return answerList;
    }

    private boolean isDuplicatedAnswer(Long userId, Long questionId ) {
        return answerRepository.findByUserIdAndQuestionId(userId,questionId).isPresent();
    }
}
