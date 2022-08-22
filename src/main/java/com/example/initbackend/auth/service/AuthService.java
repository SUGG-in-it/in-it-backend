package com.example.initbackend.auth.service;

import com.example.initbackend.auth.dto.IssueCertificationCodeRequestDto;
import com.example.initbackend.auth.repository.AuthRepository;
import com.example.initbackend.auth.domain.Auth;
import com.example.initbackend.global.util.GenerateCeritificationCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthRepository authRepository;

    // 이메일 전송 로직 추가 전 일단 res로 코드 내려줌
    public String issueCertificationCode(IssueCertificationCodeRequestDto issueCertificationCodeRequestDto){
        String certificationCode = GenerateCeritificationCode.generateCeritificationCode();
        Auth auth = issueCertificationCodeRequestDto.toEntity(certificationCode);
        authRepository.save(auth);

        return certificationCode;
    }
}
