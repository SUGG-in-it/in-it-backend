package com.example.initbackend.userToken.dto;

import com.example.initbackend.userToken.domain.UserToken;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TokenDto {

    @NotBlank
    private Long userId;

    private String refreshToken;


    public UserToken toEntity(){
        return UserToken.builder()
                .userId(userId)
                .refreshToken(refreshToken)
                .build();
    }
}
