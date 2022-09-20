package com.example.initbackend.questionTag.domain;

import com.example.initbackend.question.domain.Question;
import com.example.initbackend.tag.domain.Tag;

import javax.persistence.*;

@Entity
public class QuestionTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "question_id")
    @Column(nullable = false)
    private Question question;

    @ManyToOne
    @JoinColumn(name = "tag_id")
    @Column(nullable = false)
    private Tag tag;

}