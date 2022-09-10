package com.example.initbackend.question.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.example.initbackend.question.domain.Question;
import com.example.initbackend.question.dto.IssueQuestionIdRequestDto;
import com.example.initbackend.question.repository.QuestionRepository;
import com.example.initbackend.question.vo.IssueQuestionIdResponseVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@PropertySource("classpath:application.properties")
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final AmazonS3Client amazonS3Client;

    public IssueQuestionIdResponseVo issueQuestionId(Integer userId){
        Question question = IssueQuestionIdRequestDto.toEntity(userId);
        Question newQuestion = questionRepository.save(question);

        IssueQuestionIdResponseVo issueQuestionIdResponse = new IssueQuestionIdResponseVo(newQuestion.getId());

        return issueQuestionIdResponse;
    }
}
