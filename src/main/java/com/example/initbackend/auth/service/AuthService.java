package com.example.initbackend.auth.service;

import com.example.initbackend.auth.dto.IssueCertificationCodeRequestDto;
import com.example.initbackend.auth.repository.AuthRepository;
import com.example.initbackend.user.domain.User;
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
    private final UserRepository userRepository;

    public void issueCertificationCode(IssueCertificationCodeRequestDto issueCertificationCodeRequestDto){
        String certificationCode = GenerateCeritificationCode.generateCeritificationCode();
//        String email = issueCertificationCodeRequestDto.toEntity(certificationCode);
//        Optional<User> optionalUser = userRepository.findByEmail(email);
//        if (!optionalUser.isPresent()) {
//            throw new EntityNotFoundException(
//                    "User not present in the database"
//            );
//        }
//
//        User user = optionalUser.get();
//        user.setPassword(changePasswordRequestDto.toEntity().getPassword());
//        authRepository.save(user);
    }
}
