package com.example.initbackend.answer.service;

import com.example.initbackend.answer.domain.Answer;
import com.example.initbackend.answer.dto.UpdateAnswerRequestDto;
import com.example.initbackend.answer.repository.AnswerRepository;
import com.example.initbackend.comment.service.CommentService;
import com.example.initbackend.question.domain.Question;
import com.example.initbackend.question.repository.QuestionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.NoSuchElementException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class AnswerServiceTest {

    @Autowired
    AnswerRepository answerRepository;
    @Autowired
    AnswerService answerService;

    @Autowired
    CommentService commentService;

    @Autowired
    QuestionRepository questionRepository;

    Long testQuestionId = 20L;
    Long testUserId = 1L;

    HttpServletRequest request;

    @BeforeEach
    public void setAnswer() {


        questionRepository.save(Question.builder()
                .id(testQuestionId)
                .userId(testUserId)
                .build());

        answerRepository.save(Answer.builder()
                .questionId(testQuestionId)
                .userId(testUserId)
                .content("test answer1")
                .build());
    }

    @AfterEach
    public void resetAnswer() {

        answerRepository.deleteAnswerByQuestionIdAndUserId(testQuestionId,testUserId);
        questionRepository.deleteById(testQuestionId);
    }

    @Test
    void issueAnswerId_makeAnswer_OK() {
        //given
        Answer answer = answerRepository.save(Answer.builder()
                .questionId(testQuestionId)
                .userId(2L)
                .build());

        //when
        Answer findAnswer = answerRepository.findById(answer.getId()).orElseThrow();

        //then
        assertAll(
                () -> assertThat(findAnswer.getQuestionId()).isEqualTo(answer.getQuestionId()),
                () -> assertThat(findAnswer.getUserId()).isEqualTo(answer.getUserId()),
                () -> assertThat(findAnswer.getContent()).isEqualTo(answer.getContent()),
                () -> assertThat(findAnswer.getCreateDate()).isNotNull(),
                () -> assertThat(findAnswer.getUpdateDate()).isNotNull()
        );
    }

    @Test
    void getAnswer_getAnswerList_OK() {
        //given
        Pageable pageable = PageRequest.of(0,2);
        answerRepository.save(Answer.builder()
                .questionId(testQuestionId)
                .userId(2L)
                .content("test answer2")
                .build());

        //when

        Page<Answer> optionalAnswers = answerRepository.findByQuestionIdAndContentIsNotNullOrderByIsSelectedDesc(testQuestionId, pageable);

        //then
        assertThat(optionalAnswers.getTotalElements()).isEqualTo(2);
    }

    @Test
    void updateAnswer_updateContent_OK() {
        //given
        String testContent = "testContent";
        Answer testAnswer = answerRepository.findByQuestionIdAndUserId(testQuestionId, testUserId);
        UpdateAnswerRequestDto updateAnswerRequestDto = UpdateAnswerRequestDto.builder()
                                                                            .content(testContent)
                                                                            .build();

        //when
        answerService.updateAnswer(testUserId, updateAnswerRequestDto, testAnswer.getId());
        Answer findAnswer = answerRepository.findById(testAnswer.getId()).orElseThrow();

        //then
        assertThat(findAnswer.getContent()).isEqualTo(testContent);
    }

    @Test

    void deleteAnswer_DeleteAnswer_NoValue() {
        //given
        Answer testAnswer = answerRepository.findByQuestionIdAndUserId(testQuestionId, testUserId);

        //when
        answerService.deleteAnswer(testUserId, testAnswer.getId());

        //then
        assertThatThrownBy(() -> answerService.findAnswer(testAnswer.getId()))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void selectAnswer_SelectAnswer_OK() {
        //given
        Answer testAnswer = answerRepository.findByQuestionIdAndUserId(testQuestionId, testUserId);

        //when
        answerService.selectAnswer(testUserId, testAnswer.getId());
        Answer answer = answerRepository.findById(testAnswer.getId()).orElseThrow();

        //then
        assertTrue(answer.isSelected());
    }

    @Test
    public void getAnswersTotalPageNum_getPageNum_OK() {
        //given
        Pageable pageable = PageRequest.of(0,2);

        //when
        Page<Answer> answers = answerRepository.findByUserIdAndContentIsNotNullOrderByCreateDateDesc(testUserId, pageable);

        //then
        assertEquals(answers.getSize(), 2);
    }

    @Test
    public void getManagedAnswers_getManagedAnswers_OK()  {
        //given
        Pageable pageable = PageRequest.of(0,2);

        //when
        Page<Answer> optionalAnswers = answerRepository.findByUserIdAndContentIsNotNullOrderByCreateDateDesc(testUserId, pageable);
        //then

    }
}