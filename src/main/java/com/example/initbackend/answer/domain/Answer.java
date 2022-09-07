package com.example.initbackend.answer.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long questionId;

    @Column
    private Long userId;

    @Column
    private String content;

    @Column
    private boolean is_selected;

    @CreationTimestamp
    private Timestamp create_date;

    @UpdateTimestamp
    private Timestamp update_date;

    @Builder
    public Answer(Long id, Long questionId, Long userId, String content){
        this.id = id;
        this.questionId = questionId;
        this.userId = userId;
        this.content = content;
    }

}
