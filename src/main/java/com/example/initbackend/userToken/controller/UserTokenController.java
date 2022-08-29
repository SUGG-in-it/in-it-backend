package com.example.initbackend.userToken.controller;


import com.example.initbackend.global.jwt.dto.JwtResponseDto;
import com.example.initbackend.global.response.StatusEnum;
import com.example.initbackend.global.response.SuccessResponse;
import com.example.initbackend.userToken.service.UserTokenService;
import lombok.Getter;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Getter
@RestController
@RequestMapping("api/token")
public class UserTokenController {

    private final UserTokenService userTokenService ;

    public UserTokenController(UserTokenService userTokenService) {
        this.userTokenService = userTokenService;
    }

    @GetMapping({ "/refresh-token" })
    public SuccessResponse reIssueToken (HttpServletRequest request, HttpServletResponse response) throws IOException {

//        Cookie[] cookies = request.getCookies();
//
//        for(Cookie c : cookies) {
//            if(c.getName().equals("refreshToken")){
//                System.out.println(c.getName());
//            }
//        }

        JwtResponseDto.TokenInfo token = userTokenService.reIssueToken(request, response);
        ResponseCookie cookie = ResponseCookie.from("refreshToken", token.getRefreshToken())
                .maxAge(7 * 24 * 60 * 60)
                .path("/")
                .secure(true)
                .sameSite("None")
                .httpOnly(true)
                .build();
        response.setHeader("Set-Cookie", cookie.toString());

        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("Verified")
                .build();

        return res;
    }
}
