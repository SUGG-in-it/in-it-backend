package com.example.initbackend.user.service;

import com.example.initbackend.global.handler.CustomException;
import com.example.initbackend.global.jwt.JwtTokenProvider;
import com.example.initbackend.global.jwt.dto.JwtResponseDto;
import com.example.initbackend.global.response.ErrorCode;
import com.example.initbackend.pkg.oauth.GithubInfo;
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

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Map;
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

    public User join(JoinRequestDto joinRequestDto) {
        String email = joinRequestDto.getEmail();
        userRepository.findByEmail(email).ifPresent(u -> {
            throw new CustomException(ErrorCode.CONFLICT);
        });
        // 닉네임 중복 확인
        User user = joinRequestDto.toEntity();
        userRepository.save(user);
        return user;
    }

    public void duplicatedEmail(DuplicatedUserRequestDto duplicatedUserRequestDto) {
        String email = duplicatedUserRequestDto.getEmail();
        userRepository.findByEmail(email).ifPresent(u -> {
            throw new CustomException(ErrorCode.CONFLICT);
        });

    }

    public void duplicatedNickname(DuplicatedNicknameRequestDto duplicatedNicknameRequestDto) {
        String nickname = duplicatedNicknameRequestDto.getNickname();
        userRepository.findByNickname(nickname).ifPresent(u -> {
            throw new CustomException(ErrorCode.CONFLICT);
        });
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
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent()) {
            throw new CustomException(ErrorCode.DATA_NOT_FOUND);
        }
        String dbPassword = optionalUser.get().getPassword();
        if (!dbPassword.equals(password)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_PASSWORD);
        }

        UsernamePasswordAuthenticationToken authenticationToken = loginRequestDto.toAuthentication();
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        JwtResponseDto.TokenInfo tokenInfo = jwtTokenProvider.generateToken(optionalUser.get().getId(), optionalUser.get().getNickname(),authentication);

        UserToken userToken = tokenRepository.findById(optionalUser.get().getId());
        if (Objects.isNull(userToken)) {
            userToken = new UserToken(optionalUser.get().getId(), tokenInfo.getRefreshToken());
        } else {
            userToken.setRefreshToken(tokenInfo.getRefreshToken());
        }

        tokenRepository.save(userToken);



        return new LoginResponseVo(
                tokenInfo.getAccessToken(),
                tokenInfo.getRefreshToken()
        );

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

    @Transactional
    public LoginResponseVo loginGithubUser(String accessToken) throws IOException {

        Map<String, Object> userInfo = GithubInfo.getGithubUserInfo(accessToken);

        String email = userInfo.get("email").toString();
        String nickname = userInfo.get("nickname").toString();
        String password = "github";
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent()){
            optionalUser = Optional.ofNullable(join(JoinRequestDto.builder().email(email).password(password).nickname(nickname).build()));
        }

        Long userId = optionalUser.get().getId();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        JwtResponseDto.TokenInfo tokenInfo = jwtTokenProvider.generateToken(userId, optionalUser.get().getNickname(), authentication);

        UserToken userToken = tokenRepository.findByUserId(userId);

        if (Objects.isNull(userToken)) {
            userToken = new UserToken(userId, tokenInfo.getRefreshToken());
            tokenRepository.save(userToken);
        } else {
            userToken.setRefreshToken(tokenInfo.getRefreshToken()); // 더티체킹
        }

        return new LoginResponseVo(
                tokenInfo.getAccessToken(),
                tokenInfo.getRefreshToken()
        );
    }
}
