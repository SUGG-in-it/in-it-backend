package com.example.initbackend.likes.service;

import com.example.initbackend.global.jwt.JwtTokenProvider;
import com.example.initbackend.global.jwt.JwtUtil;
import com.example.initbackend.likes.domain.Likes;
import com.example.initbackend.likes.repository.LikesRepository;
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
    public void addLikes(HttpServletRequest request, Long questionId) {
        String token = jwtTokenProvider.resolveAccessToken(request);
        Long userId = jwtUtil.getPayloadByToken(token);

        Question question = new Question();
        question.setId(questionId);

        User user = new User();
        user.setId(userId);

        if (!IsKeyExists(questionId)) addDataFromDBtoCache(String.valueOf(questionId), getDataFromDBtoCache(question));
        addSet(String.valueOf(questionId), String.valueOf(userId));
        save(question, user);
    }

    private void save(Question question, User user){

        likesRepository.save(Likes.builder()
                .question(question)
                .user(user)
                .build());
    }

    private void addSet(String key, String values){
        redisTemplate.opsForSet().add(key, values);
    }

    private boolean IsKeyExists(Long key){
        return redisTemplate.hasKey(String.valueOf(key));
    }

    private void addDataFromDBtoCache(String key, List<Likes> values){

        values.stream().forEach(
                it -> {
                    addSet(key, String.valueOf(it.getUserId().getId()));
                }
        );
    }

    private List<Likes> getDataFromDBtoCache(Question question){
        return likesRepository.findUserIdsByQuestionId(question);
    }
}
