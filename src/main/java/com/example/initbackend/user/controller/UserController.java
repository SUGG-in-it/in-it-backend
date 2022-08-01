package com.example.initbackend.user.controller;

import com.example.initbackend.global.response.StatusEnum;
import com.example.initbackend.global.response.SuccessResponse;
import com.example.initbackend.user.controller.dto.JoinRequestDto;
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

        return res;
    }
//    public ResponseEntity<SuccessResponse> signUp(@Valid @RequestBody SignUpRequestDto requestDto) {
//        userService.join(requestDto);
//        SuccessResponse res = SuccessResponse.builder()
//                .status(StatusEnum.CREATED)
//                .message("회원가입 성공")
//                .build();
//        return new ResponseEntity<>(res, HttpStatus.CREATED);
//    }
}
