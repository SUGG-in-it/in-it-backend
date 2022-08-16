package com.example.initbackend.user.dto;

import com.example.initbackend.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class ChangePasswordRequestDto {

    @NotBlank(message = "이메일을 입력해주세요")
    @Email(message = "이메일 양식 오류")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;


    @Builder
    public ChangePasswordRequestDto(String email, String passowrd) {

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
