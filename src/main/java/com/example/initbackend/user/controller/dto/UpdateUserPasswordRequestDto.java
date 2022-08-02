package com.example.initbackend.user.controller.dto;

import com.example.initbackend.user.domain.User;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
public class UpdateUserPasswordRequestDto {

    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;

    @Builder
    public UpdateUserPasswordRequestDto(String password) {
        this.password = password;
    }

    public User toEntity(){
        return User.builder()
                .password(password)
                .build();
    }
}
