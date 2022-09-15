package com.example.initbackend.question.repository;


import com.example.initbackend.question.domain.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    Optional<Question> findById(Long questionId);
    Page<Question> findByType(String type, Pageable pageable);
    Page<Question> findByTypeNot(String type, Pageable pageable);
    Page<Question> findAll (Pageable pageable);

    Question findFirstByOrderByPointDesc();
    Question findFirstByOrderByUpdateDateDesc();
}
