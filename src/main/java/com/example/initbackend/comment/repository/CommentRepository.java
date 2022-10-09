package com.example.initbackend.comment.repository;

import com.example.initbackend.comment.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllByAnswerId(Long answerId, Pageable pageable);
    Page<Comment> findAllByUserId(Long userId, Pageable pageable);
    Page<Comment> findAllByUserIdOrderByCreateDateDesc(Long UserId, Pageable pageable);
    void deleteAllByAnswerId(Long answerId);

}