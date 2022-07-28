package com.example.initbackend.user.controller;

import org.springframework.web.bind.annotation.*;

@RequestMapping("api/user")
@RestController
public class UserController {

    @PostMapping({ "/join" })
    public String login() {
        return "Hello";
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
