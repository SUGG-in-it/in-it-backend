package com.example.initbackend.likes.service;

import com.example.initbackend.global.jwt.JwtTokenProvider;
import com.example.initbackend.global.jwt.JwtUtil;
import com.example.initbackend.likes.domain.Likes;
import com.example.initbackend.likes.repository.LikesRepository;
import com.example.initbackend.likes.vo.GetLikeResponseVo;
import com.example.initbackend.question.domain.Question;
import com.example.initbackend.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;


@Slf4j
@Service
@Transactional
@Component
@AllArgsConstructor
public class LikesService {

    private final JwtUtil jwtUtil;
    private final JwtTokenProvider jwtTokenProvider;

    private final LikesRepository likesRepository;

    private RedisTemplate<String, String> redisTemplate;

    @Transactional
    public void addLike(HttpServletRequest request, Long questionId) {
        String token = jwtTokenProvider.resolveAccessToken(request);
        Long userId = jwtUtil.getPayloadByToken(token);

        Question question = Question.createQuestion(questionId);
        User user = User.createUser(userId);

        Likes likes = likesRepository.findByQuestionAndUser(question, user);


        if (likes == null) {
            save(question, user);
        }
        addSet(String.valueOf(questionId), String.valueOf(userId));
    }

    public void cancelLike(HttpServletRequest request, Long questionId) {
        String token = jwtTokenProvider.resolveAccessToken(request);
        Long userId = jwtUtil.getPayloadByToken(token);

        Question question = Question.createQuestion(questionId);
        User user = User.createUser(userId);

        deleteSet(String.valueOf(questionId), String.valueOf(userId));
        delete(question, user);
    }

    public GetLikeResponseVo getLike(HttpServletRequest request, Long questionId) {
        String token = jwtTokenProvider.resolveAccessToken(request);
        Long userId = jwtUtil.getPayloadByToken(token);

        if (!isKeyExists(questionId)) {
            Question question = Question.createQuestion(questionId);
            addDataFromDBtoCache(String.valueOf(questionId), getDataFromDBtoCache(question));
        }

        return new GetLikeResponseVo(
                findSet(String.valueOf(questionId), String.valueOf(userId)),
                findCount(String.valueOf(questionId))
        );
    }

    private void save(Question question, User user) {

        likesRepository.save(Likes.builder()
                .question(question)
                .user(user)
                .build());
    }

    private void delete(Question question, User user) {
        likesRepository.deleteByQuestionAndUser(question, user);
    }

    private void addSet(String key, String values) {
        redisTemplate.opsForSet().add(key, values);
    }

    private void deleteSet(String key, String values) {
        redisTemplate.opsForSet().remove(key, values);
    }

    private Long findCount(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    private boolean findSet(String key, String value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    private boolean isKeyExists(Long key) {
        return redisTemplate.hasKey(String.valueOf(key));
    }

    private void addDataFromDBtoCache(String key, List<Likes> values) {

        values.stream().forEach(
                it -> {
                    addSet(key, String.valueOf(it.getUser().getId()));
                }
        );
    }

    private List<Likes> getDataFromDBtoCache(Question question) {
        return likesRepository.findUsersByQuestion(question);
    }
}
