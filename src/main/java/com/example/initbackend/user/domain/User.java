package com.example.initbackend.user.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class User {
    private Long id;
    private String email;
    private String password;
    private String nickname;
    private String year;
    private String workPosition;

    @Builder
    public User(Long id, String email, String password, String nickname, String year, String workPosition) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.year = year;
        this.password = password;
        this.workPosition = workPosition;
    }
}
