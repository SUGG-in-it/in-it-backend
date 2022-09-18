package com.example.initbackend.answer.service;

import com.example.initbackend.answer.domain.Answer;
import com.example.initbackend.answer.dto.UpdateAnswerRequestDto;
import com.example.initbackend.answer.repository.AnswerRepository;
import com.example.initbackend.answer.vo.GetAnswerResponseVo;
import com.example.initbackend.answer.vo.GetAnswersTotalPageNumResponseVo;
import com.example.initbackend.answer.vo.IssueAnswerIdResponseVo;
import com.example.initbackend.global.handler.CustomException;
import com.example.initbackend.global.jwt.JwtTokenProvider;
import com.example.initbackend.global.jwt.JwtUtil;
import com.example.initbackend.global.response.ErrorCode;
import com.example.initbackend.question.repository.QuestionRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
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

    public IssueAnswerIdResponseVo issueAnswerId(HttpServletRequest request){
        String token = jwtTokenProvider.resolveAccessToken(request);
        Long userId  = jwtUtil.getPayloadByToken(token);
        Answer newAnswer = new Answer(userId);
        answerRepository.save(newAnswer);

        return new IssueAnswerIdResponseVo(newAnswer.getId());
    }


    public GetAnswerResponseVo getAnswer(Pageable pageable, Long questionId){
        Page<Answer> optionalAnswer = answerRepository.findAllByQuestionIdOrderByCreateDateDesc(questionId, pageable);
        GetAnswerResponseVo answerList = new GetAnswerResponseVo(optionalAnswer.getContent());
        return answerList;
    }

    public void updateAnswer(UpdateAnswerRequestDto updateAnswerRequestDto, Long answerId){

        Optional<Answer> optionalAnswer = answerRepository.findById(answerId);
        optionalAnswer.ifPresentOrElse(
                selectAnswer ->{
                    selectAnswer.setContent(updateAnswerRequestDto.getContent());
                    answerRepository.save(selectAnswer);
                    },
                () -> {
                    throw new CustomException(ErrorCode.DATA_NOT_FOUND);
                });

    }

    public void deleteAnswer(Long answerId){

        Optional<Answer> optionalAnswer = answerRepository.findById(answerId);
        optionalAnswer.ifPresentOrElse(
                selectAnswer ->{
                    answerRepository.deleteById(optionalAnswer.get().getId());
                },
                () -> {
                    throw new CustomException(ErrorCode.DATA_NOT_FOUND);
                });
    }


    public void selectAnswer(Long answerId){

        Optional<Answer> optionalAnswer = answerRepository.findById(answerId);
        optionalAnswer.ifPresentOrElse(
                selectAnswer ->{
                    selectAnswer.setSelected(true);
                    answerRepository.save(selectAnswer);
                },
                () -> {
                    throw new CustomException(ErrorCode.DATA_NOT_FOUND);
                });

    }

    public GetAnswersTotalPageNumResponseVo getAnswersTotalPageNum(Pageable pageable, Long questionId){
        Page<Answer> optionalAnswer = answerRepository.findAllByQuestionIdOrderByCreateDateDesc(questionId, pageable);
        GetAnswersTotalPageNumResponseVo getAnswersTotalPageNumResponse = new GetAnswersTotalPageNumResponseVo(optionalAnswer.getTotalPages());
        return getAnswersTotalPageNumResponse;
    }

    private boolean isDuplicatedAnswer(Long userId, Long questionId ) {
        return answerRepository.findByUserIdAndQuestionId(userId,questionId).isPresent();
    }
}
