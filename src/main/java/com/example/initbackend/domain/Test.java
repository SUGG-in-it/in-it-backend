package com.example.initbackend.domain;

import javax.persistence.*;

@Entity
@Table(name = "tbl")
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200, nullable = false)
    private String memoTextselet;
}