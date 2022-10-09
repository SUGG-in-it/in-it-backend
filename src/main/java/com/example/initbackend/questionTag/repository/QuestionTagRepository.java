package com.example.initbackend.questionTag.repository;


import com.example.initbackend.questionTag.domain.QuestionTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface QuestionTagRepository extends JpaRepository<QuestionTag, Long> {

    @Transactional
    void deleteAllByQuestionId(Long questionId);

    @Query("SELECT tagId FROM QuestionTag GROUP BY tagId order by Count(tagId)")
    List<Long> countTagIdByOrderByCount();
}
