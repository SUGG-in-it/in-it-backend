package com.example.initbackend.answer.repository;

import com.example.initbackend.answer.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Component
@Repository
public interface AnswerRepository  extends JpaRepository<Answer,String> {

    Optional<Answer> findByUserIdAndQuestionId(Long userId, Long QuestionId);
    Optional<Answer> findById(Long answerId);

    @Query("SELECT questionId, COUNT(questionId) as cnt FROM Answer GROUP BY questionId order by cnt desc")
    List<Object[]> countTotalAnswersByQuestionIdByOrderByCountDesc();

}
