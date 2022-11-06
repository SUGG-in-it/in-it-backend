package com.example.initbackend.answer.repository;

import com.example.initbackend.answer.domain.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    Optional<Answer> findByUserIdAndQuestionId(Long userId, Long questionId);

    Optional<Answer> findById(Long answerId);

    List<Answer> findAllByQuestionId(Long questionId);

    @Query("SELECT questionId, COUNT(questionId) as cnt FROM Answer GROUP BY questionId order by cnt desc")
    List<Object[]> countTotalAnswersByQuestionIdByOrderByCountDesc();

    Page<Answer> findByQuestionIdAndContentIsNotNullOrderByIsSelectedDesc(Long questionId, Pageable pageable);

    Page<Answer> findByUserIdAndContentIsNotNullOrderByCreateDateDesc(Long userId, Pageable pageable);


    void deleteAllByQuestionId(Long answerId);

    Long countByQuestionId(Long questionId);
}
