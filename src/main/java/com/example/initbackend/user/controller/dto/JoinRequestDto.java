package com.example.initbackend.user.controller.dto;

import com.example.initbackend.user.domain.User;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.Getter;

@Getter
public class JoinRequestDto {
    @NotBlank(message = "이메일을 입력해주세요")
    @Email(message = "이메일 양식 오류")
    private String email;

    @NotBlank(message = "이메일을 입력해주세요")
    private String password;

    @NotBlank(message = "닉네임을 입력해주세요")
    private String nickname;

    private String year;

    private String workPosition;

    @Builder
    public JoinRequestDto(String email, String password, String nickname, String year, String workPosition) {
        this.nickname = nickname;
        this.email = email;
        this.year = year;
        this.password = password;
        this.workPosition = workPosition;
    }

    public User toEntity(){
        return User.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .year(year)
                .workPosition(workPosition)
                .build();
    }
}