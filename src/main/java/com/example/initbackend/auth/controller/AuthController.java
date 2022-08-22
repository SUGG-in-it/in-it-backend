package com.example.initbackend.auth.controller;

import com.example.initbackend.auth.dto.IssueCertificationCodeRequestDto;
import com.example.initbackend.auth.service.AuthService;
import com.example.initbackend.global.response.StatusEnum;
import com.example.initbackend.global.response.SuccessResponse;
import com.example.initbackend.user.dto.LoginRequestDto;
import com.example.initbackend.user.service.UserService;
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

    @PostMapping({ "/issue" })
    public SuccessResponse issueCertificationCode(@Valid @RequestBody final IssueCertificationCodeRequestDto requestDto) {
        String certificationCode = authService.issueCertificationCode(requestDto);
        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message(String.valueOf(certificationCode))
                .build();

        return res;
    }
}
