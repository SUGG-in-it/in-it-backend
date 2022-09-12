package com.example.initbackend.question.domain;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private Boolean isCompleted;

    @Column
    private Integer point;

    @Column
    private Long userId;

    @Column
    private Integer selectedUserId;

    @Column
    private String tagList;

    @Column
    private Integer views;

    @CreationTimestamp
    private Timestamp createDate;

    @UpdateTimestamp
    private Timestamp updateDate;

    @Builder
    public Question(String title, String content, Boolean isCompleted, Integer point, Long userId, Integer selectedUserId, String tagList, Integer views) {
        this.title = title;
        this.content = content;
        this.isCompleted = isCompleted;
        this.point = point;
        this.userId = userId;
        this.selectedUserId = selectedUserId;
        this.tagList = tagList;
        this.views = views;
    }
}