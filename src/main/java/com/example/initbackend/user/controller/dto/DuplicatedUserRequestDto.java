package com.example.initbackend.user.controller.dto;

import com.example.initbackend.user.domain.User;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
public class DuplicatedUserRequestDto {
    @NotBlank(message = "이메일을 입력해주세요")
    @Email(message = "이메일 양식 오류")
    private String email;


    @Builder
    public DuplicatedUserRequestDto(String email) {
        this.email = email;
    }

    public User toEntity(){
        return User.builder()
                .email(email)
                .build();
    }
}
