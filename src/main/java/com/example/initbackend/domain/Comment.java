package com.example.initbackend.domain;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private Integer answer_id;

    @Column(nullable = false)
    private Integer user_id;

    @Column
    private String content;

    @CreationTimestamp
    private Timestamp createDate;

    @CreationTimestamp
    private Timestamp updateDate;
}