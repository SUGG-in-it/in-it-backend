package com.example.initbackend.auth.domain;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Auth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 30)
    private String email;

    @Column(nullable = false, length = 30)
    private String code;

    @CreationTimestamp
    private Timestamp create_date;

    @CreationTimestamp
    private Timestamp update_date;

    @Builder
    public Auth(String email, String code) {
        this.email = email;
        this.code = code;
    }
}