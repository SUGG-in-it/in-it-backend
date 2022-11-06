package com.example.initbackend.question.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @Column
    private String type;

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


//    @OneToMany(mappedBy = "question")
//    private List<QuestionTag> questionTags = new ArrayList<>();

    @Builder
    public Question(String title, String content, String type, Integer point, Long userId, Integer selectedUserId, String tagList, Integer views) {
        this.title = title;
        this.content = content;
        this.type = type;
        this.point = point;
        this.userId = userId;
        this.selectedUserId = selectedUserId;
        this.tagList = tagList;
        this.views = views;
    }
}