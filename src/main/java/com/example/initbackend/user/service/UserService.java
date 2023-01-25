package com.example.initbackend.user.service;

import com.example.initbackend.global.handler.CustomException;
import com.example.initbackend.global.jwt.JwtTokenProvider;
import com.example.initbackend.global.jwt.dto.JwtResponseDto;
import com.example.initbackend.global.response.ErrorCode;
import com.example.initbackend.user.dto.*;
import com.example.initbackend.user.domain.User;
import com.example.initbackend.user.repository.UserRepository;
import com.example.initbackend.user.vo.GetProfileResponseVo;
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
import javax.transaction.Transactional;
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
        String email = joinRequestDto.getEmail();
        userRepository.findByEmail(email).ifPresent(u ->
                new CustomException(ErrorCode.CONFLICT)
        );

        User user = joinRequestDto.toEntity();
        userRepository.save(user);
    }

    public void duplicatedEmail(DuplicatedUserRequestDto duplicatedUserRequestDto) {
        String email = duplicatedUserRequestDto.getEmail();
        userRepository.findByEmail(email).ifPresent(u ->
                new CustomException(ErrorCode.CONFLICT)
        );

    }

    public void duplicatedNickname(DuplicatedNicknameRequestDto duplicatedNicknameRequestDto) {
        String nickname = duplicatedNicknameRequestDto.getNickname();
        userRepository.findByNickname(nickname).ifPresent(u ->
                new CustomException(ErrorCode.CONFLICT)
        );
    }

    @Transactional
    public void changePassword(ChangePasswordRequestDto changePasswordRequestDto) {
        String email = changePasswordRequestDto.toEntity().getEmail();
        String newPassword = changePasswordRequestDto.toEntity().getPassword();
        User optionalUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.DATA_NOT_FOUND));

        optionalUser.builder()
                .password(newPassword)
                .build();

        userRepository.save(optionalUser);
    }

    @Transactional
    public LoginResponseVo login(LoginRequestDto loginRequestDto) {

        String email = loginRequestDto.toEntity().getEmail();
        String password = loginRequestDto.toEntity().getPassword();
        User optionalUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.DATA_NOT_FOUND));

        String dbPassword = optionalUser.getPassword();
        if (!dbPassword.equals(password)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_PASSWORD);
        }

        UsernamePasswordAuthenticationToken authenticationToken = loginRequestDto.toAuthentication();
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        JwtResponseDto.TokenInfo tokenInfo = jwtTokenProvider.generateToken(optionalUser.getId(), optionalUser.getNickname(), authentication);

        UserToken userToken = tokenRepository.findById(optionalUser.getId());

        if (Objects.isNull(userToken)) {
            userToken = new UserToken(optionalUser.getId(), tokenInfo.getRefreshToken());
        } else {
            userToken.builder()
                    .refreshToken(tokenInfo.getRefreshToken())
                    .build();
        }

        tokenRepository.save(userToken);

        LoginResponseVo loginResponseVo = new LoginResponseVo(
                tokenInfo.getAccessToken(),
                tokenInfo.getRefreshToken()
        );

        return loginResponseVo;

    }

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("아이디나 비밀번호가 틀립니다."));
        user.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .nickname(user.getNickname())
                .authority(user.getRole())
                .enabled(user.isEnabled())
                .build();

        return user;
    }

    public GetProfileResponseVo getUserByNickname(String nickname) {

        User user = userRepository.findByNickname(nickname).orElseThrow(() -> new CustomException(ErrorCode.DATA_NOT_FOUND));

        GetProfileResponseVo getProfileResponseVo = new GetProfileResponseVo(
                user.getEmail(),
                user.getNickname(),
                user.getGithub_account(),
                user.getIntroduction(),
                user.getWork_position(),
                user.getCareer(),
                user.getCompany(),
                user.getPoint(),
                user.getLevel()
        );

        return getProfileResponseVo;
    }

    @Transactional
    public void updateUser(UpdateProfileRequestDto updateProfileRequestDto) {
        User optionalUser = userRepository.findByEmail(updateProfileRequestDto.getEmail()).orElseThrow(() -> new CustomException(ErrorCode.DATA_NOT_FOUND));

        User user = optionalUser.builder()
                .nickname(updateProfileRequestDto.getNickname())
                .github_account(updateProfileRequestDto.getGithubAccount())
                .introduction(updateProfileRequestDto.getIntroduction())
                .work_position(updateProfileRequestDto.getWorkPosition())
                .career(updateProfileRequestDto.getCareer())
                .company(updateProfileRequestDto.getCompany())
                .build();

        userRepository.save(user);
    }
}
