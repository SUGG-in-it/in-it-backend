package com.example.initbackend.question.service;

import com.example.initbackend.answer.repository.AnswerRepository;
import com.example.initbackend.global.jwt.JwtTokenProvider;
import com.example.initbackend.global.jwt.JwtUtil;
import com.example.initbackend.global.util.GenerateRandomNumber;
import com.example.initbackend.question.domain.Question;
import com.example.initbackend.question.dto.IssueQuestionIdRequestDto;
import com.example.initbackend.question.dto.UpdateQuestionRequestDto;
import com.example.initbackend.question.repository.QuestionRepository;
import com.example.initbackend.question.vo.GetBannerQuestionIdResponseVo;
import com.example.initbackend.question.vo.GetQuestionResponseVo;
import com.example.initbackend.question.vo.GetQuestionsResponseVo;
import com.example.initbackend.question.vo.IssueQuestionIdResponseVo;
import com.example.initbackend.user.domain.User;
import com.example.initbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@PropertySource("classpath:application.properties")
public class QuestionService {
    private final JwtUtil jwtUtil;
    private final JwtTokenProvider jwtTokenProvider;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;

    public IssueQuestionIdResponseVo issueQuestionId(HttpServletRequest request){
        String token = jwtTokenProvider.resolveAccessToken(request);
        Long userId  = jwtUtil.getPayloadByToken(token);
        Question question = IssueQuestionIdRequestDto.toEntity(userId);
        Question newQuestion = questionRepository.save(question);

        return new IssueQuestionIdResponseVo(newQuestion.getId());
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

        return new GetQuestionResponseVo(
                question.getId(),
                user.get().getId(),
                question.getTitle(),
                question.getContent(),
                user.get().getNickname(),
                user.get().getLevel(),
                question.getPoint(),
                question.getTagList(),
                question.getCreateDate(),
                question.getUpdateDate()
        );
    }

    public GetQuestionsResponseVo GetQuestions(Integer page, Integer count, String type){
        List<GetQuestionResponseVo> questionList = new ArrayList<>();
        Page<Question> questions = null;
        if (type.equals("total")){
            System.out.println("====total====");
            questions = questionRepository.findAll(PageRequest.of(page-1, count));
        }
        else if (type.equals("doing")){
            System.out.println("====doing====");
            questions = questionRepository.findByType("doing", PageRequest.of(page-1, count));
        }
        else if (type.equals("completed")){
            System.out.println("====completed====");
            questions = questionRepository.findByType("completed", PageRequest.of(page-1, count));
        }
        questions.stream().forEach(
                it -> {
                    Optional<Question> optionalQuestion = questionRepository.findById(it.getId());
                    Question question = optionalQuestion.get();
                    Long userId = question.getUserId();
                    Optional<User> user = userRepository.findById(userId);
                    GetQuestionResponseVo getQuestionResponse = new GetQuestionResponseVo(
                            question.getId(),
                            user.get().getId(),
                            question.getTitle(),
                            question.getContent(),
                            user.get().getNickname(),
                            user.get().getLevel(),
                            user.get().getPoint(),
                            question.getTagList(),
                            question.getCreateDate(),
                            question.getUpdateDate()
                    );
                    System.out.println(question.getContent() + " " + user.get().getId());
                    questionList.add(getQuestionResponse);
                    System.out.println("====3====");
                }
        );

        return new GetQuestionsResponseVo(questionList);
    }

    public void DeleteQuestion(Long questionId){
        Optional<Question> optionalQuestion = questionRepository.findById(questionId);
        String status = optionalQuestion.get().getType();
        if(status.equals("completed")){
            optionalQuestion.ifPresent(selectQuestion->{
                questionRepository.deleteById(questionId);
            });
        }
    }

    public GetBannerQuestionIdResponseVo GetBannerQuestionId(String type){
        Question question = null;
        List<Question> questions = null;
        if(type.equals("popular")){
            List<Object[]> counts = answerRepository.countTotalAnswersByQuestionIdByOrderByCountDesc();
            return new GetBannerQuestionIdResponseVo((Long) counts.get(0)[0]);
        }
        else if(type.equals("recent")){
            question = questionRepository.findFirstByOrderByUpdateDateDesc();
        }
        else if(type.equals("point")){
            question = questionRepository.findFirstByOrderByPointDesc();
        }
        else if(type.equals("random")){
            questions = questionRepository.findAll();
            Long count = questions.stream().count();
            System.out.println(count);
            Long randomId = GenerateRandomNumber.generateRandomNumber(count);
            // 이게 가끔 에러가 뜨는데 원인 파악을 좀 해봐야할듯..!
            return new GetBannerQuestionIdResponseVo(questionRepository.findById(randomId).get().getId());
        }
        return new GetBannerQuestionIdResponseVo();
    }
}
