package com.example.initbackend.question.repository;


import com.example.initbackend.question.domain.Question;
import com.example.initbackend.question.vo.SearchQuestionVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    Optional<Question> findById(Long questionId);

    Page<Question> findByTypeOrderByCreateDateDesc(String type, Pageable pageable);

    Page<Question> findByTypeNotOrderByCreateDateDesc(String type, Pageable pageable);

    Page<Question> findByUserIdOrderByCreateDateDesc(Long userId, Pageable pageable);

    Page<Question> findAll(Pageable pageable);

    Page<Question> findAllByUserIdOrderByCreateDateDesc(Long userId, Pageable pageable);


    @Query(nativeQuery = true, value = "SELECT q.id, q.title,q.content,q.update_date,q.type,q.create_date,q.point,q.user_id, q.views,q.selected_user_id, q.tag_list " +
            "FROM Question q " +
            "LEFT JOIN question_tag r " +
            "    ON q.id = r.question_id " +
            "LEFT JOIN tag t " +
            "    ON t.id = r.tag_id " +
            "WHERE t.tag IN (?1) " +
            "GROUP BY q.id " +
            "HAVING COUNT(DISTINCT t.tag) = ?2 "+
            "LIMIT ")
    Page<Question> findByTypeNotAndTitleContainingIgnoreCaseByTags(List<String> tags, Integer num, String type, String title, Pageable pageable);
    Page<Question> findByTypeNotAndTitleContainingIgnoreCase( String type, String title, Pageable pageable);


    @Query(nativeQuery = true, value = "SELECT q.id, q.title,q.content,q.update_date,q.type,q.create_date,q.point,q.user_id, q.views,q.selected_user_id, q.tag_list " +
            "FROM Question q " +
            "LEFT JOIN question_tag r " +
            "    ON q.id = r.question_id " +
            "LEFT JOIN tag t " +
            "    ON t.id = r.tag_id " +
            "WHERE t.tag IN (?1) " +
            "GROUP BY q.id " +
            "HAVING COUNT(DISTINCT t.tag) = ?2 ")
    Page<Question> findByTypeAndTitleContainingIgnoreCaseAndByTags(List<String> tags, Integer num, String type, String title, Pageable pageable);
    Page<Question> findByTypeAndTitleContainingIgnoreCase( String type, String title, Pageable pageable);


    Question findFirstByOrderByPointDesc();

    Question findFirstByOrderByUpdateDateDesc();
}
