package com.example.initbackend.user.service;

import com.example.initbackend.global.handler.CustomException;
import com.example.initbackend.global.jwt.JwtTokenProvider;
import com.example.initbackend.global.jwt.dto.JwtResponseDto;
import com.example.initbackend.global.response.ErrorCode;
import com.example.initbackend.user.dto.*;
import com.example.initbackend.user.domain.User;
import com.example.initbackend.user.repository.UserRepository;
import com.example.initbackend.user.vo.LoginResponseVo;
import com.example.initbackend.userToken.domain.UserToken;
import com.example.initbackend.userToken.repository.UserTokenRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Getter
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserTokenRepository tokenRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    public void join(JoinRequestDto joinRequestDto) {
        if (isDuplicatedUser(joinRequestDto.getEmail())) {
            throw new CustomException(ErrorCode.CONFLICT);
//                    IllegalArgumentException("Duplicated User");
        }

        User user = joinRequestDto.toEntity();
        userRepository.save(user);
    }

    public void duplicatedEmail(DuplicatedUserRequestDto duplicatedUserRequestDto) {
        if (isDuplicatedUser(duplicatedUserRequestDto.getEmail())) {
            throw new CustomException(ErrorCode.CONFLICT);
//            throw new IllegalArgumentException("Duplicated User");
        }
    }

    public void duplicatedNickname(DuplicatedNicknameRequestDto duplicatedNicknameRequestDto) {
        if (isDuplicatedNickname(duplicatedNicknameRequestDto.getNickname())) {
            throw new CustomException(ErrorCode.CONFLICT);
//            throw new IllegalArgumentException("Duplicated Nickname");
        }
    }

    public void changePassword(ChangePasswordRequestDto changePasswordRequestDto) {
        String email = changePasswordRequestDto.toEntity().getEmail();
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent()) {
            throw new CustomException(ErrorCode.DATA_NOT_FOUND);
//            throw new EntityNotFoundException("User not present in the database");
        }

        User user = optionalUser.get();
        user.setPassword(changePasswordRequestDto.toEntity().getPassword());
        userRepository.save(user);
    }


    public LoginResponseVo login(LoginRequestDto loginRequestDto) {

        String email = loginRequestDto.toEntity().getEmail();
        String password = loginRequestDto.toEntity().getPassword();
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent()) {
            throw new CustomException(ErrorCode.CONFLICT);
//            throw new EntityNotFoundException("User not present in the database");
        }
        String dbPassword = optionalUser.get().getPassword();
        if (!dbPassword.equals(password)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_PASSWORD);
//            throw new EntityNotFoundException("incorrect password");
        }

        UsernamePasswordAuthenticationToken authenticationToken = loginRequestDto.toAuthentication();
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        JwtResponseDto.TokenInfo tokenInfo = jwtTokenProvider.generateToken(optionalUser.get().getId(), authentication);

        UserToken userToken;
        userToken = tokenRepository.findById(optionalUser.get().getId());
        if (Objects.isNull(userToken)) {
            userToken = new UserToken(optionalUser.get().getId(), tokenInfo.getRefreshToken());
        } else {
            userToken.setRefreshToken(tokenInfo.getRefreshToken());
        }

        tokenRepository.save(userToken);

        LoginResponseVo loginResponseVo = new LoginResponseVo(
                tokenInfo.getAccessToken(),
                tokenInfo.getRefreshToken()
        );

        return loginResponseVo;

    }

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> byEmail = userRepository.findByEmail(email);

        User user = byEmail.orElseThrow(() -> new UsernameNotFoundException("아이디나 비밀번호가 틀립니다."));

        return User.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .nickname(user.getNickname())
                .authority(user.getRole())
                .enabled(user.isEnabled())
                .build();

    }

    private boolean isDuplicatedUser(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    private boolean isDuplicatedNickname(String nickname) {
        return userRepository.findByNickname(nickname).isPresent();
    }
}
