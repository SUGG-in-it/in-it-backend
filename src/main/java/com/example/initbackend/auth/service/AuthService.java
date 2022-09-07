package com.example.initbackend.auth.service;

import com.example.initbackend.auth.domain.Auth;
import com.example.initbackend.auth.dto.IssueCertificationCodeRequestDto;
import com.example.initbackend.auth.dto.VerifyCertificationCodeRequestDto;
import com.example.initbackend.auth.repository.AuthRepository;
import com.example.initbackend.global.handler.CustomException;
import com.example.initbackend.global.response.ErrorCode;
import com.example.initbackend.global.util.GenerateCeritificationCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;
import java.util.Calendar;
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
        String email = issueCertificationCodeRequestDto.getEmail();
        Auth auth = issueCertificationCodeRequestDto.toEntity(certificationCode);
        Optional<Auth> optionalAuth = authRepository.findByEmail(email);
        if (!optionalAuth.isPresent()) {
            authRepository.save(auth);
        } else {
            Auth newAuth = optionalAuth.get();
            newAuth.setCode(certificationCode);
            newAuth.setUpdate_date(new Timestamp(System.currentTimeMillis()));
            authRepository.save(newAuth);
        }
        return certificationCode;
    }

    public void verifyCertificationCodeRequestDto(VerifyCertificationCodeRequestDto verifyCertificationCodeRequestDto){
        String certificationCode = verifyCertificationCodeRequestDto.getCode();
        String email = verifyCertificationCodeRequestDto.getEmail();
        Optional<Auth> optionalAuth = authRepository.findByEmail(email);
        if (!optionalAuth.isPresent()) {
            throw new CustomException(ErrorCode.DATA_NOT_FOUND);
//            throw new EntityNotFoundException(
//                    "Email not present in the database"
//            );
        }

        Timestamp currnetTime = new Timestamp(System.currentTimeMillis());
        Timestamp issuedTime = optionalAuth.get().getUpdate_date();

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(issuedTime.getTime());

        // add 30 seconds
        cal.add(Calendar.MINUTE, 5);
        issuedTime = new Timestamp(cal.getTime().getTime());
        System.out.println("=========Current Time==========");
        System.out.println(currnetTime);
        System.out.println("=========Issued Time==========");
        System.out.println(issuedTime);


        if(currnetTime.after(issuedTime)){
            throw new CustomException(ErrorCode.CERTIFICATION_CODE_EXPIRED);
//            throw new EntityNotFoundException(
//                    "Code Expired"
//            );
        }

        String dbCertificationCode = optionalAuth.get().getCode();
        if(!dbCertificationCode.equals(certificationCode)){
            throw new CustomException(ErrorCode.UNAUTHORIZED_CERTIFICATION_CODE);
//            throw new EntityNotFoundException(
//                    "incorrect certificationCode"
//            );
        }

        // 시간 초과 시 에러처리 필요


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
