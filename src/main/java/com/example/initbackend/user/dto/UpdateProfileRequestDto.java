package com.example.initbackend.user.dto;

import com.example.initbackend.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileRequestDto {

    @Email(message = "이메일 양식 오류")
    private String email;
    private String nickname;
    private String githubAccount;
    private String introduction;
    private String workPosition;
    private String career;
    private String company;
    private Integer point;
    private String level;

}
