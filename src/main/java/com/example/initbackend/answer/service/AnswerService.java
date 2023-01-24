package com.example.initbackend.answer.service;

import com.example.initbackend.answer.domain.Answer;
import com.example.initbackend.answer.dto.IssueAnswerIdDto;
import com.example.initbackend.answer.dto.UpdateAnswerRequestDto;
import com.example.initbackend.answer.repository.AnswerRepository;
import com.example.initbackend.answer.vo.*;
import com.example.initbackend.comment.repository.CommentRepository;
import com.example.initbackend.global.handler.CustomException;
import com.example.initbackend.global.jwt.JwtTokenProvider;
import com.example.initbackend.global.jwt.JwtUtil;
import com.example.initbackend.global.response.ErrorCode;
import com.example.initbackend.question.domain.Question;
import com.example.initbackend.question.repository.QuestionRepository;
import com.example.initbackend.user.domain.User;
import com.example.initbackend.user.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Getter
@Service
@RequiredArgsConstructor
public class AnswerService {

    private final JwtUtil jwtUtil;
    private final JwtTokenProvider jwtTokenProvider;

    private final UserRepository userRepository;

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final CommentRepository commentRepository;

    public IssueAnswerIdResponseVo issueAnswerId(HttpServletRequest request, IssueAnswerIdDto issueAnswerIdDto) {
        String token = jwtTokenProvider.resolveAccessToken(request);
        Long userId = jwtUtil.getPayloadByToken(token);
        Long questionId = issueAnswerIdDto.getQuestionId();
        Answer ans= Answer.builder()
                .userId(userId)
                .questionId(questionId)
                .build();

        answerRepository.save(ans);
        return new IssueAnswerIdResponseVo(ans.getId());
    }


    public List<GetAnswerResponseVo> getAnswer(Pageable pageable, Long questionId) {
        Page<Answer> optionalAnswer = answerRepository.findByQuestionIdAndContentIsNotNullOrderByIsSelectedDesc(questionId, pageable);

        List<GetAnswerResponseVo> answers = new ArrayList<>();

        for (Answer answer : optionalAnswer) {

            Optional<User> optionalUser = userRepository.findById(answer.getUserId());

            if (!optionalUser.isPresent()) {
                throw new CustomException(ErrorCode.DATA_NOT_FOUND);
            }
            GetAnswerResponseVo vo = new GetAnswerResponseVo(
                    answer.getId(),
                    answer.getUserId(),
                    optionalUser.get().getNickname(),
                    answer.getContent(),
                    answer.isSelected(),
                    answer.getCreateDate(),
                    answer.getUpdateDate()
            );
            answers.add(vo);

        }
        return answers;
    }

    public void updateAnswer(HttpServletRequest request, UpdateAnswerRequestDto updateAnswerRequestDto, Long answerId) {

        String token = jwtTokenProvider.resolveAccessToken(request);
        Long userId = jwtUtil.getPayloadByToken(token);

        Optional<Answer> optionalAnswer = answerRepository.findById(answerId);

        if (!optionalAnswer.isPresent()) {
            throw new CustomException(ErrorCode.DATA_NOT_FOUND);
        }

        if (!userId.equals(optionalAnswer.get().getUserId())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        optionalAnswer.ifPresent(selectAnswer -> {
            selectAnswer.setContent(updateAnswerRequestDto.getContent());
            answerRepository.save(selectAnswer);
        });

    }

    @Transactional
    public void deleteAnswer(HttpServletRequest request, Long answerId) {

        String token = jwtTokenProvider.resolveAccessToken(request);
        Long userId = jwtUtil.getPayloadByToken(token);

        Optional<Answer> optionalAnswer = answerRepository.findById(answerId);

        if (!optionalAnswer.isPresent()) {
            throw new CustomException(ErrorCode.DATA_NOT_FOUND);
        }

        if (!userId.equals(optionalAnswer.get().getUserId())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        optionalAnswer.ifPresent(selectAnswer -> {
            answerRepository.deleteById(answerId);
            commentRepository.deleteAllByAnswerId(answerId);
        });
    }


    public void selectAnswer(HttpServletRequest request, Long answerId) {

        String token = jwtTokenProvider.resolveAccessToken(request);
        Long userId = jwtUtil.getPayloadByToken(token);

        Optional<Answer> optionalAnswer = answerRepository.findById(answerId);

        if (!optionalAnswer.isPresent()) {
            throw new CustomException(ErrorCode.DATA_NOT_FOUND);
        }

        Optional<Question> optionalQuestion = questionRepository.findById(optionalAnswer.get().getQuestionId());

        if (!optionalQuestion.isPresent()) {
            throw new CustomException(ErrorCode.DATA_NOT_FOUND);
        }

        if (!userId.equals(optionalQuestion.get().getUserId())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        optionalAnswer.ifPresent(selectAnswer -> {
            selectAnswer.setSelected(true);
            answerRepository.save(selectAnswer);
        });

        optionalQuestion.ifPresent(selectQuestion -> {
            selectQuestion.setType("completed");
            questionRepository.save(selectQuestion);
        });


    }

    public GetAnswersTotalPageNumResponseVo getAnswersTotalPageNum(Pageable pageable, Long questionId) {
        Page<Answer> optionalAnswer = answerRepository.findByQuestionIdAndContentIsNotNullOrderByIsSelectedDesc(questionId, pageable);
        GetAnswersTotalPageNumResponseVo getAnswersTotalPageNumResponse = new GetAnswersTotalPageNumResponseVo(optionalAnswer.getTotalPages());
        return getAnswersTotalPageNumResponse;
    }

    public GetUserAnswersTotalPageNumResponseVo getUserAnswersTotalPageNum(HttpServletRequest request, Pageable pageable) {

        String token = jwtTokenProvider.resolveAccessToken(request);
        Long userId = JwtUtil.getPayloadByToken(token);

        Page<Answer> answers = answerRepository.findByUserIdAndContentIsNotNullOrderByCreateDateDesc(userId, pageable);

        GetUserAnswersTotalPageNumResponseVo getUserAnswersTotalPageNumResponse = new GetUserAnswersTotalPageNumResponseVo(answers.getTotalPages());
        return getUserAnswersTotalPageNumResponse;
    }

    public GetManagedAnswersResponseVo getManagedAnswers(HttpServletRequest servletRequest, Pageable pageable) {
        String token = jwtTokenProvider.resolveAccessToken(servletRequest);
        Long userId = JwtUtil.getPayloadByToken(token);

        List<ManagedAnswerVo> managedAnswerList = new ArrayList<>();
        Page<Answer> optionalAnswers = answerRepository.findByUserIdAndContentIsNotNullOrderByCreateDateDesc(userId, pageable);

        optionalAnswers.stream().forEach(
                answer -> {
                    Optional<Question> optionalQuestion = questionRepository.findById(answer.getQuestionId());
                    if (!optionalQuestion.isPresent()) {
                        throw new CustomException(ErrorCode.DATA_NOT_FOUND);
                    }
                    Question question = optionalQuestion.get();

                    ManagedAnswerVo managedAnswerVo = new ManagedAnswerVo(
                            answer.getId(),
                            answer.getQuestionId(),
                            question.getTitle(),
                            answer.getUserId(),
                            answer.getContent(),
                            answer.isSelected(),
                            answer.getCreateDate(),
                            answer.getUpdateDate()
                    );
                    managedAnswerList.add(managedAnswerVo);
                }
        );

        return new GetManagedAnswersResponseVo(managedAnswerList);
    }


    private boolean isDuplicatedAnswer(Long userId, Long questionId) {
        return answerRepository.findByUserIdAndQuestionId(userId, questionId).isPresent();
    }
}
