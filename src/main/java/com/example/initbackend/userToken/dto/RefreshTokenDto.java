package com.example.initbackend.userToken.dto;

import com.example.initbackend.userToken.domain.UserToken;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RefreshTokenDto {

    private String refreshToken;


    public UserToken toEntity(){
        return UserToken.builder()
                .refreshToken(refreshToken)
                .build();
    }
}
