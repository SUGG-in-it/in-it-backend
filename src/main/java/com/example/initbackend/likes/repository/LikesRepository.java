package com.example.initbackend.likes.repository;

import com.example.initbackend.likes.domain.Likes;
import com.example.initbackend.question.domain.Question;
import com.example.initbackend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {

    List<Likes> findUsersByQuestion(Question question);

    Likes findByQuestionAndUser(Question question, User user);

    void deleteByQuestionAndUser(Question question, User user);

    Long countByQuestion(Question question);
}
