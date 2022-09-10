package com.example.initbackend.user.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetProfileResponseVo {

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
