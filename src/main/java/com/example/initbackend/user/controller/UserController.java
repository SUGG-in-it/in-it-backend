package com.example.initbackend.user.controller;

import com.example.initbackend.global.response.StatusEnum;
import com.example.initbackend.global.response.SuccessResponse;
import com.example.initbackend.pkg.oauth.GithubInfo;
import com.example.initbackend.user.dto.*;
import com.example.initbackend.user.service.UserService;
import com.example.initbackend.user.vo.GetProfileResponseVo;
import com.example.initbackend.user.vo.LoginResponseVo;
import lombok.Getter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Getter
@RestController
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;

    @Value("${spring.security.oauth2.client.registration.github.clientId}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.github.clientSecret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.github.redirectURL}")
    private String redirectURL;

    @Value("${spring.security.oauth2.client.registration.github.redirectPrefix}")
    private String redirectPrefix;


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

    @GetMapping({ "/login/github" })
    public ResponseEntity<?> loginWithGithub() {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(String.format("%s?client_id=%s&redirect_uri=%s", redirectPrefix, clientId, redirectURL)));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    @GetMapping({ "/login/redirect/github" })
    public ResponseEntity<?> getGithubAccessToken(@Valid @RequestParam String code) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("client_id", clientId);
        map.put("client_secret", clientSecret);
        map.put("code", code);

        String AccessToken = GithubInfo.getGithubAccessToken(map);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(String.format("/api/users/login/github/think?accessToken=%s", AccessToken)));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    @GetMapping({ "/login/github/think" })
    public SuccessResponse loginGithubUser(@Valid @RequestParam String accessToken) throws IOException {
        System.out.println("========리다이렉트 성공==========");
        LoginResponseVo loginGithubUserResponseVo = userService.loginGithubUser(accessToken);
        // 2. 회원 가입 (없을 경우 로그인) -> service
        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("GithubUser Logged in")
                .data(loginGithubUserResponseVo)
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
