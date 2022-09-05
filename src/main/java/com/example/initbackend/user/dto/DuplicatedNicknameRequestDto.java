package com.example.initbackend.user.dto;

import com.example.initbackend.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class DuplicatedNicknameRequestDto {
    @NotBlank(message = "닉네임을 입력해주세요")
    private String nickname;


    @Builder
    public DuplicatedNicknameRequestDto(String nickname) {
        this.nickname = nickname;
    }

    public User toEntity(){
        return User.builder()
                .nickname(nickname)
                .build();
    }
}
