package com.example.initbackend.user.controller;

import com.example.initbackend.global.jwt.dto.JwtResponseDto;
import com.example.initbackend.global.response.StatusEnum;
import com.example.initbackend.global.response.SuccessResponse;
import com.example.initbackend.user.controller.dto.ChangePasswordRequestDto;
import com.example.initbackend.user.controller.dto.DuplicatedUserRequestDto;
import com.example.initbackend.user.controller.dto.JoinRequestDto;
import com.example.initbackend.user.controller.dto.LoginRequestDto;
import com.example.initbackend.user.service.UserService;
import lombok.Getter;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Getter
@RestController
@RequestMapping("api/user")
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
    public SuccessResponse login(@Valid @RequestBody final LoginRequestDto requestDto) {
        String token = userService.login(requestDto);
        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message(String.valueOf(token))
                .build();

        return res;
    }
}
