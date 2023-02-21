package com.example.initbackend.answer.service;

import com.example.initbackend.answer.domain.Answer;
import com.example.initbackend.answer.dto.UpdateAnswerRequestDto;
import com.example.initbackend.answer.repository.AnswerRepository;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
    HttpServletRequest request;

    Long testQuestionId = 100L;
    Long testUserId = 1L;



    @BeforeEach
    public void setAnswer() {
        Answer answer = Answer.builder()
                .questionId(testQuestionId)
                .userId(testUserId)
                .content("test answer1")
                .build();

        answerRepository.save(answer);
    }

    @AfterEach
    public void resetAnswer() {
        answerRepository.deleteAnswerByQuestionIdAndUserId(testQuestionId,testUserId);
    }

    @Test
    void issueAnswerId_makeAnswer_OK() {
        //given
        Answer ans= Answer.builder()
                .questionId(testQuestionId)
                .userId(2L)
                .build();
        //when
        Answer answer = answerRepository.save(ans);
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

        Answer answer1 = Answer.builder()
                .questionId(testQuestionId)
                .userId(2L)
                .content("test answer2")
                .build();

        //when
        answerRepository.save(answer1);
        Page<Answer> optionalAnswers = answerRepository.findByQuestionIdAndContentIsNotNullOrderByIsSelectedDesc(testQuestionId, pageable);

        //then
        assertThat(optionalAnswers.getTotalElements()).isEqualTo(2);
    }

    @Test
    void updateAnswer() {
        //given
        String testContent = "testContent";
        UpdateAnswerRequestDto updateAnswerRequestDto = UpdateAnswerRequestDto.builder()
                                                                            .content(testContent)
                                                                            .build();
        //when
        answerService.updateAnswer(request, updateAnswerRequestDto, 5L);
        Answer findAnswer = answerRepository.findById(5L).orElseThrow();

        //then
        assertThat(findAnswer.getContent()).isEqualTo(testContent);
    }

    @Test
    void deleteAnswer() {
        //given

        //when

        //then
    }

    @Test
    void selectAnswer() {
        //given

        //when

        //then
    }
}