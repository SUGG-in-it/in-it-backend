package com.example.initbackend.user.controller.dto;

import com.example.initbackend.user.domain.User;
import lombok.Builder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class ChangePasswordDto {


    @NotBlank(message = "이메일을 입력해주세요")
    @Email(message = "이메일 양식 오류")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;


    @Builder
    public ChangePasswordDto(String email, String passowrd) {

        this.email = email;
        this.password = passowrd;
    }

    public User toEntity(){
        return User.builder()
                .email(email)
                .password(password)
                .build();
    }
}
