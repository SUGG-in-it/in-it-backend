package com.example.initbackend.domain;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "auth")
public class Auth {

    @Column(nullable = false)
    private Integer user_id;

    @Column(nullable = false)
    private String code;

    @CreationTimestamp
    private Timestamp createDate;

    @CreationTimestamp
    private Timestamp updateDate;
}