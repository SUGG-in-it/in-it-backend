package com.example.initbackend.auth.controller;

import com.example.initbackend.auth.dto.IssueCertificationCodeRequestDto;
import com.example.initbackend.auth.dto.VerifyCertificationCodeRequestDto;
import com.example.initbackend.auth.service.AuthService;
import com.example.initbackend.auth.vo.IssueCertificationCodeResponseVo;
import com.example.initbackend.global.response.StatusEnum;
import com.example.initbackend.global.response.SuccessResponse;
import lombok.Getter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Getter
@RestController
@RequestMapping("api/auth")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping({ "" })
    public SuccessResponse checkAccessToken() {

        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .build();

        return res;
    }

    @PostMapping({ "/issue" })
    public SuccessResponse issueCertificationCode(@Valid @RequestBody final IssueCertificationCodeRequestDto requestDto) {
        String certificationCode = authService.issueCertificationCode(requestDto);
        IssueCertificationCodeResponseVo issueCertificationCodeResponseVo = authService.sendSimpleMessage(requestDto.getEmail(), certificationCode);
        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("Issued CertificationCode")
                .data(issueCertificationCodeResponseVo)
                .build();

        return res;
    }

    @PostMapping({ "/verify" })
    public SuccessResponse verifyCertificationCode(@Valid @RequestBody final VerifyCertificationCodeRequestDto requestDto) {
        authService.verifyCertificationCodeRequestDto(requestDto);
        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("Verified")
                .build();

        return res;
    }
}
