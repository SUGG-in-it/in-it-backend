package com.example.initbackend.auth.dto;

import com.example.initbackend.auth.domain.Auth;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Getter
public class IssueCertificationCodeRequestDto {
    @NotBlank(message = "이메일을 입력해주세요")
    @Email(message = "이메일 양식 오류")
    private String email;

    @NotBlank
    private String type;

    public IssueCertificationCodeRequestDto(String email, String type) {
        this.email = email;
        this.type = type;
    }
    public Auth toEntity(String certificationCode){
        return Auth.builder()
                .email(email)
                .code(certificationCode)
                .build();
    }
}
