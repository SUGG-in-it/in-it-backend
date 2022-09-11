package com.example.initbackend.question.service;

import com.example.initbackend.question.domain.Question;
import com.example.initbackend.question.dto.IssueQuestionIdRequestDto;
import com.example.initbackend.question.dto.UpdateQuestionRequestDto;
import com.example.initbackend.question.repository.QuestionRepository;
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
        String email = updateQuestionRequestDto.getEmail();
        Optional<User> optionalUser = userRepository.findByEmail(email);
        Optional<Question> optionalQuestion = questionRepository.findById(questionId);
        Long userId = optionalUser.get().getId();
        System.out.println("=====================");
        System.out.println(userId);
        System.out.println("=====================");
        // 이렇게 update 하는게 맞을까요? 아닌가봅니다...
        Question question = optionalQuestion.get();
        question.setTitle(UpdateQuestionRequestDto.toEntity(userId).getTitle());
        question.setContent(UpdateQuestionRequestDto.toEntity(userId).getContent());
        question.setTag_list(UpdateQuestionRequestDto.toEntity(userId).getTag_list());
        question.setPoint(UpdateQuestionRequestDto.toEntity(userId).getPoint());

        questionRepository.save(question);
    }
}
