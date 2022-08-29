package com.example.initbackend.userToken.service;

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

    public JwtResponseDto.TokenInfo reIssueToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String token = jwtTokenProvider.resolveAccessToken(request);
            // 만료된 Access Token을 디코딩하여 Payload 값을 가져옴
            HashMap<String, String> payloadMap = JwtUtil.getPayloadByToken(token);
            String email = payloadMap.get("sub");

            Optional<UserToken> refreshToken = tokenRepository.findByEmail(email);
            refreshToken.orElseThrow(
                    () -> new CustomException(ErrorCode.UNAUTHORIZED)
            );

            // Refresh Token이 만료가 된 토큰인지 확인합니다
            boolean isTokenValid = jwtTokenProvider.validateToken(refreshToken.get().getRefreshToken());

            // Refresh Token이 만료가 되지 않은 경우
            if(isTokenValid) {
                Optional<User> user = userRepository.findByEmail(email);

                if(user.isPresent()) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, user.get().getPassword());
                    Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
                    JwtResponseDto.TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
                    UserToken userToken;

                    Optional<UserToken> optionalToken = tokenRepository.findByEmail(user.get().getEmail());
                    if (!optionalToken.isPresent()) {
                        userToken = new UserToken();
                        userToken.setEmail(user.get().getEmail());
                    } else{
                        userToken = optionalToken.get();
                    }

                    userToken.setRefreshToken(tokenInfo.getRefreshToken());
                    tokenRepository.save(userToken);


                    return tokenInfo;
                }
            }
        } catch(ExpiredJwtException e) {
            // Refresh Token 만료 Exception
            throw new CustomException(ErrorCode.JWT_REFRESH_TOKEN_EXPIRED);
        }
        return null;
    }
}
