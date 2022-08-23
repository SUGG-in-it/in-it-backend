

package com.example.initbackend.auth.dto;

import com.example.initbackend.auth.domain.Auth;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class VerifyCertificationCodeRequestDto {
    @NotBlank(message = "이메일을 입력해주세요")
    @Email(message = "이메일 양식 오류")
    private String email;

    @NotBlank(message = "인증 코드를 입력해주세요")
    private String code;


    public VerifyCertificationCodeRequestDto(String email, String code) {
        this.email = email;
        this.code = code;
    }
    public Auth toEntity(){
        return Auth.builder()
                .email(email)
                .code(code)
                .build();
    }
}
