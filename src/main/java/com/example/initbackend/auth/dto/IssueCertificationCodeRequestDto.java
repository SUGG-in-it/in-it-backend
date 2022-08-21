package com.example.initbackend.auth.dto;

import com.example.initbackend.auth.domain.Auth;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class IssueCertificationCodeRequestDto {
    @NotBlank(message = "이메일을 입력해주세요")
    @Email(message = "이메일 양식 오류")
    private String email;

    public IssueCertificationCodeRequestDto(String email) {
        this.email = email;
    }
    public Auth toEntity(String certificationCode){
        return Auth.builder()
                .email(email)
                .code(certificationCode)
                .build();
    }
}
