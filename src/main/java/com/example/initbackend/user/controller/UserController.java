package com.example.initbackend.user.controller;

import org.springframework.web.bind.annotation.*;

@RequestMapping("api/user")
@RestController
public class UserController {

    @PostMapping({ "/login" })
    public String login() {
        return "Hello";
    }
}
