package com.example.initbackend.question.domain;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

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

    @Column(nullable = false)
    private Boolean is_completed;

    @Column
    private Integer point;

    @Column
    private Integer user_id;

    @Column
    private Integer selected_user_id;

    @Column
    private String tag_list;

    @Column
    private Integer views;

    @CreationTimestamp
    private Timestamp create_date;

    @CreationTimestamp
    private Timestamp update_date;

    @Builder
    public Question(String title, String content, Boolean is_completed, Integer point, Integer user_id, Integer selected_user_id, String tag_list, Integer views) {
        this.title = title;
        this.content = content;
        this.is_completed = is_completed;
        this.point = point;
        this.user_id = user_id;
        this.selected_user_id = selected_user_id;
        this.tag_list = tag_list;
        this.views = views;
    }
}