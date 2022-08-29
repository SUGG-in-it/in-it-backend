package com.example.initbackend.user.service;

import com.example.initbackend.global.jwt.JwtTokenProvider;
import com.example.initbackend.global.jwt.dto.JwtResponseDto;
import com.example.initbackend.user.dto.ChangePasswordRequestDto;
import com.example.initbackend.user.dto.DuplicatedUserRequestDto;
import com.example.initbackend.user.dto.JoinRequestDto;
import com.example.initbackend.user.dto.LoginRequestDto;
import com.example.initbackend.user.domain.User;
import com.example.initbackend.user.repository.UserRepository;
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

    public void join(JoinRequestDto joinRequestDto){
        if (isDuplicatedUser(joinRequestDto.getEmail())){
            throw new IllegalArgumentException("Duplicated User");
        }
        if (isDuplicatedNickname(joinRequestDto.getNickname())){
            throw new IllegalArgumentException("Duplicated Nickname");
        }
        User user = joinRequestDto.toEntity();
        userRepository.save(user);
    }
    public void duplicatedEmail(DuplicatedUserRequestDto duplicatedUserRequestDto){
        if (isDuplicatedUser(duplicatedUserRequestDto.getEmail())){
            throw new IllegalArgumentException("Duplicated User");
        }
    }

    public void changePassword(ChangePasswordRequestDto changePasswordRequestDto){
        String email = changePasswordRequestDto.toEntity().getEmail();
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent()) {
            throw new EntityNotFoundException(
                    "User not present in the database"
            );
        }

        User user = optionalUser.get();
        user.setPassword(changePasswordRequestDto.toEntity().getPassword());
        userRepository.save(user);
    }


    public JwtResponseDto.TokenInfo login(LoginRequestDto loginRequestDto){
        String email = loginRequestDto.toEntity().getEmail();
        String password = loginRequestDto.toEntity().getPassword();
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent()) {
            throw new EntityNotFoundException(
                    "User not present in the database"
            );
        }
        String dbPassword = optionalUser.get().getPassword();
        if(!dbPassword.equals(password)){
            throw new EntityNotFoundException(
                    "incorrect password"
            );
        }

        UsernamePasswordAuthenticationToken authenticationToken = loginRequestDto.toAuthentication();
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        JwtResponseDto.TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

        UserToken userToken;

        Optional<UserToken> optionalToken = tokenRepository.findByEmail(optionalUser.get().getEmail());
        if (!optionalToken.isPresent()) {
            userToken = new UserToken();
            userToken.setEmail(optionalUser.get().getEmail());
        } else{
            userToken = optionalToken.get();
        }

        userToken.setRefreshToken(tokenInfo.getRefreshToken());
        tokenRepository.save(userToken);


        return tokenInfo;

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
