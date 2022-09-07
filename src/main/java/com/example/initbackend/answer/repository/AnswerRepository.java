package com.example.initbackend.answer.repository;

import com.example.initbackend.answer.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Component
@Repository
public interface AnswerRepository  extends JpaRepository<Answer,String> {

    Optional<Answer> findByUserIdAndQuestionId(Long userId, Long QuestionId);
}
