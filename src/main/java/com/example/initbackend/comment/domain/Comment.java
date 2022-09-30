package com.example.initbackend.comment.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long answerId;

    @Column
    private Long userId;

    @Column(nullable = false)
    private String content;

    @CreationTimestamp
    private Timestamp createdAt;

    @CreationTimestamp
    private Timestamp updatedAt;

    @CreationTimestamp
    private Timestamp deletedAt;

    @Builder
    public Comment(String content, Long userId, Long answerId) {
        this.content = content;
        this.userId = userId;
        this.answerId = answerId;
    }
}