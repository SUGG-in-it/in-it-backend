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
    private Long answer_id;

    @Column(nullable = false)
    private Long user_id;

    @Column(nullable = false)
    private String content;

    @CreationTimestamp
    private Timestamp created_at;

    @CreationTimestamp
    private Timestamp updated_at;

    @CreationTimestamp
    private Timestamp deleted_at;

    @Builder
    public Comment(String content, Long user_id, Long answer_id) {
        this.content = content;
        this.user_id = user_id;
        this.answer_id = answer_id;
    }
}