package com.example.initbackend.answer.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@NoArgsConstructor
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long questionId;

    @Column
    private Long userId;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @Column
    private boolean isSelected;

    @CreationTimestamp
    private Timestamp createDate;

    @UpdateTimestamp
    private Timestamp updateDate;

    public Answer(Long userId) {
        this.userId = userId;
    }

    public void setContent(String content){
        this.content = content;
    }

    public void setIsSelected(boolean isSelected){
        this.isSelected = isSelected;
    }

    @Builder
    public Answer(Long id, Long questionId, Long userId, String content, boolean isSelected){
        this.id = id;
        this.questionId = questionId;
        this.userId = userId;
        this.content = content;
        this.isSelected = isSelected;
    }


}
