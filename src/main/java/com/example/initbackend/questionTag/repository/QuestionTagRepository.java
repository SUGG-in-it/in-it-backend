package com.example.initbackend.questionTag.repository;


import com.example.initbackend.questionTag.domain.QuestionTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionTagRepository extends JpaRepository<QuestionTag, Long> {
}
