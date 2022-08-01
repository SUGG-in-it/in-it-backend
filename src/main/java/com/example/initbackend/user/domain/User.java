package com.example.initbackend.user.domain;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    private String year;

    @Column
    private String work_position;

    @Column
    private String company;

    @Column
    private String career;

    @Column
    private Integer auth_type;

    @ColumnDefault("0")
    private Integer point;

    @Column
    private String level;

    @CreationTimestamp
    private Timestamp create_date;

    @CreationTimestamp
    private Timestamp update_date;

    @Builder
    public User(String email, String password, String nickname, String year, String work_position) {
        this.nickname = nickname;
        this.email = email;
        this.year = year;
        this.password = password;
        this.work_position = work_position;
    }
}