package com.example.initbackend.comment.repository;

import com.example.initbackend.answer.domain.Answer;
import com.example.initbackend.comment.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
//    @Query("SELECT questionId, COUNT(questionId) as cnt FROM Answer GROUP BY questionId order by cnt desc")
//    List<Object[]> countTotalAnswersByQuestionIdByOrderByCountDesc();
//
//    Page<Answer> findAllByQuestionIdOrderByCreateDateDesc(Long questionId, Pageable pageable);
//

}