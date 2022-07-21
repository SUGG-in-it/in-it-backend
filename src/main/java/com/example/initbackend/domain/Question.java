package com.example.initbackend.domain;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "question")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private Integer user_id;

    @Column
    private Integer point;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private String tag_list;

    @Column
    private Boolean is_completed;

    @Column
    private Integer selected_user_id;

    @Column
    private Integer views;

    @CreationTimestamp
    private Timestamp createDate;

    @CreationTimestamp
    private Timestamp updateDate;
}