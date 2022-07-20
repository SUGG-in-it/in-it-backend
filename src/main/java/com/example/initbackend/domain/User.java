package com.example.initbackend.domain;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 30)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 30)
    private String nickname;

    @Column(length = 30)
    private String github_account;

    @Column
    private String introduction;

    @Column
    private Integer year;

    @Column
    private String work_position;

    @Column
    private String company;

    @Column
    private String career;

    @Column(nullable = false)
    private Integer authType;

    @ColumnDefault("0")
    private Integer point;

    @ColumnDefault("basic")
    private String level;

    @CreationTimestamp
    private Timestamp createDate;

    @CreationTimestamp
    private Timestamp updateDate;
}