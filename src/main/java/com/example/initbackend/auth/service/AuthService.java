package com.example.initbackend.auth.service;

import com.example.initbackend.auth.domain.Auth;
import com.example.initbackend.auth.dto.IssueCertificationCodeRequestDto;
import com.example.initbackend.auth.dto.VerifyCertificationCodeRequestDto;
import com.example.initbackend.auth.repository.AuthRepository;
import com.example.initbackend.auth.vo.IssueCertificationCodeResponseVo;
import com.example.initbackend.global.handler.CustomException;
import com.example.initbackend.global.response.ErrorCode;
import com.example.initbackend.global.util.GenerateCeritificationCode;
import com.example.initbackend.user.domain.User;
import com.example.initbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
//@AllArgsConstructor

public class AuthService {
    private final AuthRepository authRepository;
    private final UserRepository userRepository;
    @Autowired
    private final JavaMailSender emailSender;

    @Transactional
    public String issueCertificationCode(IssueCertificationCodeRequestDto issueCertificationCodeRequestDto){
        String type = issueCertificationCodeRequestDto.getType();
        String certificationCode = GenerateCeritificationCode.generateCeritificationCode();
        String email = issueCertificationCodeRequestDto.getEmail();
        Auth auth = issueCertificationCodeRequestDto.toEntity(certificationCode);
        Optional<Auth> optionalAuth = authRepository.findByEmail(email);
        if(type.equals("password")) {
            userRepository.findByEmail(email).orElseThrow(() -> new CustomException(ErrorCode.DATA_NOT_FOUND));
        }
        if(optionalAuth.isPresent()){
            Auth newAuth = optionalAuth.get();
            newAuth.setCode(certificationCode);
            newAuth.setUpdate_date(new Timestamp(System.currentTimeMillis()));
            authRepository.save(newAuth);
        } else {
            authRepository.save(auth);
        }
        return certificationCode;
    }

    @Transactional
    public void verifyCertificationCodeRequestDto(VerifyCertificationCodeRequestDto verifyCertificationCodeRequestDto){
        String certificationCode = verifyCertificationCodeRequestDto.getCode();
        String email = verifyCertificationCodeRequestDto.getEmail();
        Auth optionalAuth = authRepository.findByEmail(email).orElseThrow(() -> new CustomException(ErrorCode.DATA_NOT_FOUND));


        Timestamp currnetTime = new Timestamp(System.currentTimeMillis());
        Timestamp issuedTime = optionalAuth.getUpdate_date();

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
        }

        String dbCertificationCode = optionalAuth.getCode();
        if(!dbCertificationCode.equals(certificationCode)){
            throw new CustomException(ErrorCode.UNAUTHORIZED_CERTIFICATION_CODE);
        }
    }

    public IssueCertificationCodeResponseVo sendSimpleMessage(String email, String certificationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("jihyoungkwon@gmail.com");
        message.setTo(email);
        message.setSubject("인증번호");
        message.setText(certificationCode);
        emailSender.send(message);

        IssueCertificationCodeResponseVo issueCertificationCodeResponseVo = new IssueCertificationCodeResponseVo(
            certificationCode
        );
        return issueCertificationCodeResponseVo;
    }
}
