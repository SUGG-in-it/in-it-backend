package com.example.initbackend.user.controller;

import com.example.initbackend.global.jwt.dto.JwtResponseDto;
import com.example.initbackend.global.response.StatusEnum;
import com.example.initbackend.global.response.SuccessResponse;
import com.example.initbackend.user.dto.ChangePasswordRequestDto;
import com.example.initbackend.user.dto.DuplicatedUserRequestDto;
import com.example.initbackend.user.dto.JoinRequestDto;
import com.example.initbackend.user.dto.LoginRequestDto;
import com.example.initbackend.user.service.UserService;
import lombok.Getter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Getter
@RestController
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping({ "/join" })
    public SuccessResponse join(@Valid @RequestBody final JoinRequestDto requestDto) {
        userService.join(requestDto);
        // 실패시 로직 추가 요망
        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("Success Join")
                .build();
        System.out.println(res);
        return res;
    }

    @PostMapping({ "/duplicate-email" })
    public SuccessResponse duplicatedEmail(@Valid @RequestBody final DuplicatedUserRequestDto requestDto) {
        userService.duplicatedEmail(requestDto);
        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("User is not duplicated")
                .build();

        return res;
    }

    @PostMapping({ "/password" })
    public SuccessResponse changePassword(@Valid @RequestBody final ChangePasswordRequestDto requestDto) {
        userService.changePassword(requestDto);
        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("change password")
                .build();

        return res;
    }

    @PostMapping({ "/login" })
    public SuccessResponse login(@Valid @RequestBody final LoginRequestDto requestDto , HttpServletResponse response) {
        JwtResponseDto.TokenInfo token = userService.login(requestDto);

        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("로그인 성공")
                .data(token.getAccessToken())
                .build();

        return res;
    }

    @PostMapping({ "/logout" })
    public SuccessResponse logout(HttpServletResponse response) {

        ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                .path("/")
                .build();
        response.setHeader("Set-Cookie", cookie.toString());


        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("로그아웃 성공")
                .build();

        return res;
    }
}
