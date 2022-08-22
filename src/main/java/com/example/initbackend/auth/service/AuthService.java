package com.example.initbackend.auth.service;

import com.example.initbackend.auth.dto.IssueCertificationCodeRequestDto;
import com.example.initbackend.auth.repository.AuthRepository;
import com.example.initbackend.auth.domain.Auth;
import com.example.initbackend.global.util.GenerateCeritificationCode;
import com.example.initbackend.user.dto.ChangePasswordRequestDto;
import com.example.initbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthRepository authRepository;

    public void issueCertificationCode(IssueCertificationCodeRequestDto issueCertificationCodeRequestDto){
        String certificationCode = GenerateCeritificationCode.generateCeritificationCode();
        Auth auth = issueCertificationCodeRequestDto.toEntity(certificationCode);
        authRepository.save(auth);
    }
}
