package com.example.initbackend.user.controller;

import com.example.initbackend.global.jwt.dto.JwtResponseDto;
import com.example.initbackend.global.response.StatusEnum;
import com.example.initbackend.global.response.SuccessResponse;
import com.example.initbackend.question.vo.IssueQuestionIdResponseVo;
import com.example.initbackend.user.dto.*;
import com.example.initbackend.user.service.UserService;
import com.example.initbackend.user.vo.GetProfileResponseVo;
import com.example.initbackend.user.vo.LoginResponseVo;
import lombok.Getter;

import org.springframework.http.ResponseCookie;
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
        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("Success Join")
                .build();
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

    @PostMapping({ "/duplicate-nickname" })
    public SuccessResponse duplicatedNickname(@Valid @RequestBody final DuplicatedNicknameRequestDto requestDto) {
        userService.duplicatedNickname(requestDto);
        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("Nickname Is Not Duplicated")
                .build();

        return res;
    }

    @PostMapping({ "/password" })
    public SuccessResponse changePassword(@Valid @RequestBody final ChangePasswordRequestDto requestDto) {
        userService.changePassword(requestDto);
        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("Change password")
                .build();

        return res;
    }

    @PostMapping({ "/login" })
    public SuccessResponse login(@Valid @RequestBody final LoginRequestDto requestDto) {
        LoginResponseVo loginResponseVo = userService.login(requestDto);

        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("Login success")
                .data(loginResponseVo)
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
                .message("Logout success")
                .build();

        return res;
    }


    @GetMapping({"/profiles/{nickname}"})
    public SuccessResponse getProfile(@PathVariable("nickname") String nickname){

        GetProfileResponseVo getProfileResponseVo = userService.getUserByNickname(nickname);

        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("Get Profile")
                .data(getProfileResponseVo)
                .build();

        return res;
    }

    @PutMapping({"/profiles"})
    public SuccessResponse updateProfile(@Valid @RequestBody final UpdateProfileRequestDto requestDto){

        userService.updateUser(requestDto);

        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("Update Profile")
                .build();

        return res;
    }
}
