package com.example.initbackend.likes.repository;

import com.example.initbackend.likes.domain.Likes;
import com.example.initbackend.question.domain.Question;
import com.example.initbackend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {

    List<Likes> findUserIdsByQuestionId(Question questionId);
}
