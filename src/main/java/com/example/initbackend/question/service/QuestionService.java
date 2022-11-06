package com.example.initbackend.question.service;

import com.example.initbackend.answer.domain.Answer;
import com.example.initbackend.answer.repository.AnswerRepository;
import com.example.initbackend.comment.repository.CommentRepository;
import com.example.initbackend.global.handler.CustomException;
import com.example.initbackend.global.jwt.JwtTokenProvider;
import com.example.initbackend.global.jwt.JwtUtil;
import com.example.initbackend.global.response.ErrorCode;
import com.example.initbackend.global.util.GenerateRandomNumber;
import com.example.initbackend.question.domain.Question;
import com.example.initbackend.question.dto.IssueQuestionIdRequestDto;
import com.example.initbackend.question.dto.UpdateQuestionRequestDto;
import com.example.initbackend.question.repository.QuestionRepository;
import com.example.initbackend.question.vo.*;
import com.example.initbackend.questionTag.domain.QuestionTag;
import com.example.initbackend.questionTag.repository.QuestionTagRepository;
import com.example.initbackend.tag.domain.Tag;
import com.example.initbackend.tag.repository.TagRepository;
import com.example.initbackend.user.domain.User;
import com.example.initbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@PropertySource("classpath:application.properties")
public class QuestionService {
    private final JwtUtil jwtUtil;
    private final JwtTokenProvider jwtTokenProvider;
    private final QuestionRepository questionRepository;
    private final QuestionTagRepository questionTagRepository;
    private final TagRepository tagRepository;
    private final AnswerRepository answerRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public IssueQuestionIdResponseVo issueQuestionId(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveAccessToken(request);
        Long userId = jwtUtil.getPayloadByToken(token);
        Question question = IssueQuestionIdRequestDto.toEntity(userId);
        Question newQuestion = questionRepository.save(question);

        return new IssueQuestionIdResponseVo(newQuestion.getId());
    }

    public void UpdateQuestion(HttpServletRequest request, Long questionId, UpdateQuestionRequestDto updateQuestionRequestDto) {
        String token = jwtTokenProvider.resolveAccessToken(request);
        Long userId = jwtUtil.getPayloadByToken(token);

        Optional<Question> optionalQuestion = questionRepository.findById(questionId);
        if (!userId.equals(optionalQuestion.get().getUserId())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        if (!optionalQuestion.isPresent()) {
            throw new CustomException(ErrorCode.DATA_NOT_FOUND);
        }

        String tag = updateQuestionRequestDto.getTagList();
        tag = tag.replace("[", "");
        tag = tag.replace("]", "");
        tag = tag.replace(" ", "");
        tag = tag.replaceAll("\"", "");
        System.out.println(tag);

        String[] tagList = tag.split(",");

        questionTagRepository.deleteAllByQuestionId(questionId);

        if (tagList.length > 5) {
            throw new CustomException(ErrorCode.TAGLIST_TOO_LONG);
        }

        for (int i = 0; i < tagList.length; i++) {
            System.out.println(tagList[i]);
            Optional<Tag> optionalTag = tagRepository.findByTag(tagList[i]);
            if (!optionalTag.isPresent()) {
                throw new CustomException(ErrorCode.DATA_NOT_FOUND);
            }

            QuestionTag questionTag = new QuestionTag();
            questionTag.setQuestionId(optionalQuestion.get().getId());
            questionTag.setTagId(optionalTag.get().getId());
            questionTagRepository.save(questionTag);
        }

        optionalQuestion.ifPresent(selectQuestion -> {
            selectQuestion.setTitle(updateQuestionRequestDto.getTitle());
            selectQuestion.setContent(updateQuestionRequestDto.getContent());
            selectQuestion.setTagList(updateQuestionRequestDto.getTagList());
            selectQuestion.setPoint(updateQuestionRequestDto.getPoint());
            if (!selectQuestion.getType().equals("completed")) {
                selectQuestion.setType("doing");
            }
            questionRepository.save(selectQuestion);
        });
    }

    public GetQuestionResponseVo GetQuestion(Long questionId) {
        Optional<Question> optionalQuestion = questionRepository.findById(questionId);
        if (!optionalQuestion.isPresent()) {
            throw new CustomException(ErrorCode.DATA_NOT_FOUND);
        }

        Question question = optionalQuestion.get();
        Long userId = question.getUserId();
        Optional<User> user = userRepository.findById(userId);
        Long answerCount = answerRepository.countByQuestionId(question.getId());
        return new GetQuestionResponseVo(
                question.getId(),
                user.get().getId(),
                question.getTitle(),
                question.getContent(),
                user.get().getNickname(),
                user.get().getLevel(),
                question.getPoint(),
                question.getTagList(),
                question.getType(),
                question.getCreateDate(),
                question.getUpdateDate(),
                Math.toIntExact(answerCount)
        );
    }

    public GetQuestionsResponseVo GetQuestions(Pageable pageable, String type) {
        List<GetQuestionResponseVo> questionList = new ArrayList<>();
        List<Question> newQuestions = new ArrayList<>();
        Page<Question> questions = new PageImpl<>(newQuestions);

        if (type.equals("total")) {
            questions = questionRepository.findByTypeNotOrderByCreateDateDesc("init", pageable);
        } else if (type.equals("doing")) {
            questions = questionRepository.findByTypeOrderByCreateDateDesc("doing", pageable);
        } else if (type.equals("completed")) {
            questions = questionRepository.findByTypeOrderByCreateDateDesc("completed", pageable);
        }
        questions.stream().forEach(
                it -> {
                    Optional<Question> optionalQuestion = questionRepository.findById(it.getId());
                    Question question = optionalQuestion.get();
                    Long userId = question.getUserId();
                    Optional<User> user = userRepository.findById(userId);
                    if (!user.isPresent()) {
                        throw new CustomException(ErrorCode.DATA_NOT_FOUND);
                    }
                    GetQuestionResponseVo getQuestionResponse = new GetQuestionResponseVo(
                            question.getId(),
                            user.get().getId(),
                            question.getTitle(),
                            question.getContent(),
                            user.get().getNickname(),
                            user.get().getLevel(),
                            question.getPoint(),
                            question.getTagList(),
                            question.getType(),
                            question.getCreateDate(),
                            question.getUpdateDate(),
                            0
                    );
                    System.out.println(question.getContent() + " " + user.get().getId());
                    questionList.add(getQuestionResponse);
                }
        );

        return new GetQuestionsResponseVo(questionList);
    }

    @Transactional
    public void DeleteQuestion(HttpServletRequest request, Long questionId) {
        String token = jwtTokenProvider.resolveAccessToken(request);
        Long userId = jwtUtil.getPayloadByToken(token);

        Optional<Question> optionalQuestion = questionRepository.findById(questionId);
        if (!userId.equals(optionalQuestion.get().getUserId())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        optionalQuestion.ifPresent(selectQuestion -> {
            List<Answer> optionalAnswer = answerRepository.findAllByQuestionId(questionId);
            optionalAnswer.stream().forEach(
                    answer -> {
                        commentRepository.deleteAllByAnswerId(answer.getId());
                    });
            answerRepository.deleteAllByQuestionId(questionId);
            questionRepository.deleteById(questionId);
        });
    }

    public GetBannerQuestionIdResponseVo GetBannerQuestionId(String type) {
        Question question = new Question();
        List<Question> questions = new ArrayList<>();
        if (type.equals("popular")) {
            try {
                List<Object[]> counts = answerRepository.countTotalAnswersByQuestionIdByOrderByCountDesc();
                return new GetBannerQuestionIdResponseVo((Long) counts.get(0)[0]);
            } catch (IndexOutOfBoundsException e) {
                throw new CustomException(ErrorCode.StatusGone);
            }

        } else if (type.equals("recent")) {
            question = questionRepository.findFirstByOrderByUpdateDateDesc();
            return new GetBannerQuestionIdResponseVo((Long) question.getId());
        } else if (type.equals("point")) {
            question = questionRepository.findFirstByOrderByPointDesc();
            return new GetBannerQuestionIdResponseVo((Long) question.getId());
        } else if (type.equals("random")) {
            questions = questionRepository.findAll();
            Long count = questions.stream().count();

            System.out.println(count);
            Long randomId = GenerateRandomNumber.generateRandomNumber(count);
            return new GetBannerQuestionIdResponseVo(questionRepository.findById(randomId).get().getId());
        }
        return new GetBannerQuestionIdResponseVo();
    }

    public GetQuestionsTotalPageNumResponseVo GetQuestionsTotalPageNum(Pageable pageable, String type) {
        List<GetQuestionResponseVo> questionList = new ArrayList<>();
        Page<Question> questions = null;
        if (type.equals("total")) {
            questions = questionRepository.findByTypeNotOrderByCreateDateDesc("init", pageable);
        } else if (type.equals("doing")) {
            questions = questionRepository.findByTypeOrderByCreateDateDesc("doing", pageable);
        } else if (type.equals("completed")) {
            questions = questionRepository.findByTypeOrderByCreateDateDesc("completed", pageable);
        }

        GetQuestionsTotalPageNumResponseVo getQuestionsTotalPageNumResponse = new GetQuestionsTotalPageNumResponseVo(questions.getTotalPages());

        return getQuestionsTotalPageNumResponse;

    }

    public GetUserQuestionsTotalPageNumResponseVo GetUserQuestionsTotalPageNum(HttpServletRequest request, Pageable pageable) {

        String token = jwtTokenProvider.resolveAccessToken(request);
        Long userId = JwtUtil.getPayloadByToken(token);

        Page<Question> questions = questionRepository.findAllByUserIdOrderByCreateDateDesc(userId, pageable);

        GetUserQuestionsTotalPageNumResponseVo getUserQuestionsTotalPageNumResponseVo = new GetUserQuestionsTotalPageNumResponseVo(questions.getTotalPages());

        return getUserQuestionsTotalPageNumResponseVo;

    }

    public GetQuestionsResponseVo getManagedQuestions(HttpServletRequest servletRequest, Pageable pageable) {
        String token = jwtTokenProvider.resolveAccessToken(servletRequest);
        Long userId = JwtUtil.getPayloadByToken(token);

        List<GetQuestionResponseVo> questionList = new ArrayList<>();
        Page<Question> questions = null;

        questions = questionRepository.findByUserIdAndTypeNotOrderByCreateDateDesc(userId, "init", pageable);

        questions.stream().forEach(
                it -> {
                    Optional<Question> optionalQuestion = questionRepository.findById(it.getId());
                    Question question = optionalQuestion.get();
                    Optional<User> user = userRepository.findById(question.getUserId());
                    if (!user.isPresent()) {
                        throw new CustomException(ErrorCode.DATA_NOT_FOUND);
                    }
                    GetQuestionResponseVo getQuestionResponse = new GetQuestionResponseVo(
                            question.getId(),
                            user.get().getId(),
                            question.getTitle(),
                            question.getContent(),
                            user.get().getNickname(),
                            user.get().getLevel(),
                            user.get().getPoint(),
                            question.getTagList(),
                            question.getType(),
                            question.getCreateDate(),
                            question.getUpdateDate(),
                            0
                    );
                    questionList.add(getQuestionResponse);
                }
        );

        return new GetQuestionsResponseVo(questionList);
    }



    public SearchQuestionsResponseVo searchQuestions(String query, String type, Pageable pageable, String tag) {
        List<SearchQuestionVo> questionList = new ArrayList<>();

        List<Question> newQuestions = new ArrayList<>();
        Page<Question> questions = new PageImpl<>(newQuestions);
        System.out.println("====query====");
        System.out.println(query);
        if(ObjectUtils.isEmpty(query)){
            if (type.equals("total")) {
                questions = questionRepository.findByTypeNotOrderByCreateDateDesc("init", pageable);
            } else if (type.equals("doing")) {
                questions = questionRepository.findByTypeOrderByCreateDateDesc("doing", pageable);
            } else if (type.equals("completed")) {
                questions = questionRepository.findByTypeOrderByCreateDateDesc("completed", pageable);
            }
        }
        else {
            if (!ObjectUtils.isEmpty(tag)) {

                List<String> tags = Arrays.asList(tag.split(","));

                if (type.equals("total")) {
                    questions = questionRepository.findByTypeNotAndTitleContainingIgnoreCaseByTags(tags, tags.size(), "init", query, pageable);
                } else if (type.equals("doing")) {
                    questions = questionRepository.findByTypeAndTitleContainingIgnoreCaseAndByTags(tags, tags.size(), "doing", query, pageable);
                } else if (type.equals("completed")) {
                    questions = questionRepository.findByTypeAndTitleContainingIgnoreCaseAndByTags(tags, tags.size(), "completed", query, pageable);
                }
            } else {
                if (type.equals("total")) {
                    questions = questionRepository.findByTypeNotAndTitleContainingIgnoreCase("init", query, pageable);
                } else if (type.equals("doing")) {
                    questions = questionRepository.findByTypeAndTitleContainingIgnoreCase("doing", query, pageable);
                } else if (type.equals("completed")) {
                    questions = questionRepository.findByTypeAndTitleContainingIgnoreCase("completed", query, pageable);
                }
            }
        }

        questions.stream().forEach(
                it -> {
                    Optional<Question> optionalQuestion = questionRepository.findById(it.getId());
                    Question question = optionalQuestion.get();
                    Long userId = question.getUserId();
                    Optional<User> user = userRepository.findById(userId);
                    if (!user.isPresent()) {
                        throw new CustomException(ErrorCode.DATA_NOT_FOUND);
                    }
                    SearchQuestionVo searchQuestionVo = new SearchQuestionVo(
                            question.getId(),
                            user.get().getId(),
                            question.getTitle(),
                            question.getContent(),
                            user.get().getNickname(),
                            user.get().getLevel(),
                            question.getTagList(),
                            question.getPoint(),
                            question.getType(),
                            question.getCreateDate(),
                            question.getUpdateDate(),
                            0
                    );
                    System.out.println(question.getContent() + " " + user.get().getId());
                    questionList.add(searchQuestionVo);
                }
        );

        return new SearchQuestionsResponseVo(questionList);

    }


    public GetQuestionsTotalPageNumResponseVo GetSearchQuestionsTotalPageNum(String query, String type, Pageable pageable, String tag) {
        List<SearchQuestionVo> questionList = new ArrayList<>();
        List<String> tags = Arrays.asList(tag.split(","));

        System.out.println(tags);
        System.out.println(tags);
        System.out.println(tags);
        System.out.println(tags);
        System.out.println(tags.size());
        System.out.println(tags.size());
        System.out.println(tags.size());
        System.out.println(tags.size());
        Page<Question> questions = null;
        if (tags.get(0).length() != 0) {
            if (type.equals("total")) {
                questions = questionRepository.findByTypeNotAndTitleContainingIgnoreCaseByTags(tags, tags.size(), "init", query, pageable);
            } else if (type.equals("doing")) {
                questions = questionRepository.findByTypeAndTitleContainingIgnoreCaseAndByTags(tags, tags.size(), "doing", query, pageable);
            } else if (type.equals("completed")) {
                questions = questionRepository.findByTypeAndTitleContainingIgnoreCaseAndByTags(tags, tags.size(), "completed", query, pageable);
            }
        } else {
            if (type.equals("total")) {
                questions = questionRepository.findByTypeNotAndTitleContainingIgnoreCase("init", query, pageable);
            } else if (type.equals("doing")) {
                questions = questionRepository.findByTypeAndTitleContainingIgnoreCase("doing", query, pageable);
            } else if (type.equals("completed")) {
                questions = questionRepository.findByTypeAndTitleContainingIgnoreCase("completed", query, pageable);
            }
        }

        GetQuestionsTotalPageNumResponseVo getQuestionsTotalPageNumResponse = new GetQuestionsTotalPageNumResponseVo(questions.getTotalPages());

        return getQuestionsTotalPageNumResponse;

    }

}