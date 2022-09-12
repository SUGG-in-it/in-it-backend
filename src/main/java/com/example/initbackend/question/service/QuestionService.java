package com.example.initbackend.question.service;

import com.example.initbackend.question.domain.Question;
import com.example.initbackend.question.dto.IssueQuestionIdRequestDto;
import com.example.initbackend.question.dto.UpdateQuestionRequestDto;
import com.example.initbackend.question.repository.QuestionRepository;
import com.example.initbackend.question.vo.GetQuestionResponseVo;
import com.example.initbackend.question.vo.IssueQuestionIdResponseVo;
import com.example.initbackend.user.domain.User;
import com.example.initbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@PropertySource("classpath:application.properties")
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;

    public IssueQuestionIdResponseVo issueQuestionId(Long userId){
        Question question = IssueQuestionIdRequestDto.toEntity(userId);
        Question newQuestion = questionRepository.save(question);

        IssueQuestionIdResponseVo issueQuestionIdResponse = new IssueQuestionIdResponseVo(newQuestion.getId());

        return issueQuestionIdResponse;
    }

    public void UpdateQuestion(Long questionId, UpdateQuestionRequestDto updateQuestionRequestDto){
        Optional<Question> optionalQuestion = questionRepository.findById(questionId);

        optionalQuestion.ifPresent(selectQuestion->{
            selectQuestion.setTitle(updateQuestionRequestDto.getTitle());
            selectQuestion.setContent(updateQuestionRequestDto.getContent());
            selectQuestion.setTagList(updateQuestionRequestDto.getTagList());
            selectQuestion.setPoint(updateQuestionRequestDto.getPoint());

            questionRepository.save(selectQuestion);
        });
    }

    public GetQuestionResponseVo GetQuestion(Long questionId){
        Optional<Question> optionalQuestion = questionRepository.findById(questionId);
        Question question = optionalQuestion.get();
        Long userId = question.getUserId();
        Optional<User> user = userRepository.findById(userId);

        GetQuestionResponseVo getQuestionResponse = new GetQuestionResponseVo(
                question.getId(),
                question.getTitle(),
                question.getContent(),
                user.get().getNickname(),
                user.get().getLevel(),
                user.get().getPoint(),
                question.getTagList(),
                question.getCreateDate(),
                question.getUpdateDate()
        );

        return getQuestionResponse;
    }
}
