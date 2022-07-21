package com.example.initbackend.domain;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "user_token")
public class UserToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private Integer user_id;

    @Column(nullable = false)
    private String refresh_token;

    @CreationTimestamp
    private Timestamp create_date;

    @CreationTimestamp
    private Timestamp update_date;
}