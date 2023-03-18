package com.example.initbackend.userToken.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.stereotype.Component;


import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Component

public class UserToken  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column
    private String refreshToken;

    @CreationTimestamp
    private Timestamp create_date;

    @CreationTimestamp
    private Timestamp update_date;

    public void setRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }

    @Builder
    public UserToken(Long userId, String refreshToken) {

        this.userId = userId;
        this.refreshToken = refreshToken;

    }


}