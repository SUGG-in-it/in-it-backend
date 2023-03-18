package com.example.initbackend.userToken.service;

import com.example.initbackend.userToken.dto.RefreshTokenDto;
import com.example.initbackend.userToken.vo.ReIssueTokenResponseVo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.jsonwebtoken.ExpiredJwtException;
import com.example.initbackend.global.handler.CustomException;
import com.example.initbackend.global.jwt.JwtAuthenticationFilter;
import com.example.initbackend.global.jwt.JwtTokenProvider;
import com.example.initbackend.global.jwt.JwtUtil;
import com.example.initbackend.global.jwt.dto.JwtResponseDto;
import com.example.initbackend.global.response.ErrorCode;
import com.example.initbackend.user.domain.User;
import com.example.initbackend.user.repository.UserRepository;
import com.example.initbackend.userToken.domain.UserToken;
import com.example.initbackend.userToken.repository.UserTokenRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@Slf4j
@Getter
@Service
@Component
@RequiredArgsConstructor
public class UserTokenService {

    private final UserToken userToken;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final UserTokenRepository tokenRepository;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public ReIssueTokenResponseVo reIssueToken(HttpServletRequest request, RefreshTokenDto requestDto) throws IOException {
        try {
            String token = jwtTokenProvider.resolveAccessToken(request);
            // Access Token을 디코딩하여 Payload 값을 가져옴
            Long userId  = JwtUtil.getPayloadByToken(token);
            UserToken userToken = tokenRepository.findById(userId);
            if (userToken == null){
                throw new CustomException(ErrorCode.UNAUTHORIZED);
            }

            // 리프레시가 만료됐는지
            boolean isTokenValid = jwtTokenProvider.validateToken(userToken.getRefreshToken());
            // 리프레시가 만료가 되지 않은 경우 && 디비에 저장되어있을 경우
            String oldRefreshToken = requestDto.toEntity().getRefreshToken();

            if(isTokenValid && (oldRefreshToken.equals(userToken.getRefreshToken()))) {
                Optional<User> user = userRepository.findById(userId);

                if(user.isPresent()) {
                    Authentication authentication = jwtTokenProvider.getAuthentication(token);
                    JwtResponseDto.TokenInfo newTokenInfo = jwtTokenProvider.generateToken(user.get().getId(), user.get().getNickname(),authentication);
                    userToken.setRefreshToken(newTokenInfo.getRefreshToken());
                    tokenRepository.save(userToken);

                    ReIssueTokenResponseVo reIssueTokenResponseVo = new ReIssueTokenResponseVo(newTokenInfo.getAccessToken());
                    return reIssueTokenResponseVo;
                }
            }
            throw new CustomException(ErrorCode.JWT_UNAUTHORIZED);
        } catch(ExpiredJwtException e) {
            // Refresh Token 만료 Exception
            throw new CustomException(ErrorCode.JWT_REFRESH_TOKEN_EXPIRED);
        }
    }

    public JwtResponseDto.TokenInfo issueToken(String email, String password, Long userId, String nickname){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        JwtResponseDto.TokenInfo tokenInfo = jwtTokenProvider.generateToken(userId, nickname, authentication);

        UserToken userToken = tokenRepository.findByUserId(userId);

        if (userToken == null) {
            userToken = new UserToken(userId, tokenInfo.getRefreshToken());
            tokenRepository.save(userToken);
        } else {
            userToken.setRefreshToken(tokenInfo.getRefreshToken()); // 더티체킹
        }

        return tokenInfo;
    }
}
