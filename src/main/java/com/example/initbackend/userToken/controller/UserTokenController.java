package com.example.initbackend.userToken.controller;


import com.example.initbackend.global.jwt.dto.JwtResponseDto;
import com.example.initbackend.global.response.StatusEnum;
import com.example.initbackend.global.response.SuccessResponse;
import com.example.initbackend.userToken.dto.RefreshTokenDto;
import com.example.initbackend.userToken.service.UserTokenService;
import com.example.initbackend.userToken.vo.ReIssueTokenResponseVo;
import lombok.Getter;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@Getter
@RestController
@RequestMapping("api/token")
public class UserTokenController {

    private final UserTokenService userTokenService ;

    public UserTokenController(UserTokenService userTokenService) {
        this.userTokenService = userTokenService;
    }

    @PostMapping({ "/refresh-token" })
    public SuccessResponse reIssueToken (HttpServletRequest request,@Valid @RequestBody RefreshTokenDto requestDto) throws IOException {

        ReIssueTokenResponseVo reIssueTokenResponseVo = userTokenService.reIssueToken(request, requestDto);

        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .message("re-issue token")
                .data(reIssueTokenResponseVo)
                .build();

        return res;
    }
}
