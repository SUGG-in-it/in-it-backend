package com.example.initbackend.auth.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class IssueCertificationCodeResponseVo {
    @JsonProperty
    private String certificationCode;
    public IssueCertificationCodeResponseVo(String certificationCode) {

        this.certificationCode = certificationCode;
    }
}
