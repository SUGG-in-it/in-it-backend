package com.example.initbackend.auth.service;

import com.example.initbackend.auth.domain.Auth;
import com.example.initbackend.auth.dto.IssueCertificationCodeRequestDto;
import com.example.initbackend.auth.dto.VerifyCertificationCodeRequestDto;
import com.example.initbackend.auth.repository.AuthRepository;
import com.example.initbackend.global.util.GenerateCeritificationCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
//@AllArgsConstructor

public class AuthService {
    private final AuthRepository authRepository;
    @Autowired
    private final JavaMailSender emailSender;

    public String issueCertificationCode(IssueCertificationCodeRequestDto issueCertificationCodeRequestDto){
        String certificationCode = GenerateCeritificationCode.generateCeritificationCode();
        Auth auth = issueCertificationCodeRequestDto.toEntity(certificationCode);
        authRepository.save(auth);
        // 이미 존재하는 이메일인 경우 update로 수정

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
        // 삭제 로직 필요


    }

    public void sendSimpleMessage(String email, String certificationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("jihyoungkwon@gmail.com");
        message.setTo(email);
        message.setSubject("인증번호");
        message.setText(certificationCode);
        emailSender.send(message);
    }
}
