package com.example.initbackend.auth.service;

import com.example.initbackend.auth.dto.IssueCertificationCodeRequestDto;
import com.example.initbackend.auth.dto.VerifyCertificationCodeRequestDto;
import com.example.initbackend.auth.repository.AuthRepository;
import com.example.initbackend.auth.domain.Auth;
import com.example.initbackend.global.util.GenerateCeritificationCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.sql.Time;
import java.util.Optional;

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

    public void verifyCertificationCodeRequestDto(VerifyCertificationCodeRequestDto verifyCertificationCodeRequestDto){
        String certificationCode = verifyCertificationCodeRequestDto.getCode();
        String email = verifyCertificationCodeRequestDto.getEmail();
        Optional<Auth> optionalAuth = authRepository.findByEmail(email);
        if (!optionalAuth.isPresent()) {
            throw new EntityNotFoundException(
                    "Email not present in the database"
            );
        }

        String dbCertificationCode = optionalAuth.get().getCode();
        if(!dbCertificationCode.equals(certificationCode)){
            throw new EntityNotFoundException(
                    "incorrect certificationCode"
            );
        }

        // 시간 초과 시 에러처리 필요

    }
}
