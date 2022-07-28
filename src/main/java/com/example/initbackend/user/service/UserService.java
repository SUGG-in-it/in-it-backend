package com.example.initbackend.user.service;

import com.example.initbackend.user.controller.dto.JoinRequestDto;
import com.example.initbackend.user.domain.User;
import com.example.initbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void join(JoinRequestDto dto){
        if (isDuplicatedUser(dto.getEmail())){
            throw new IllegalArgumentException("Duplicated User");
        }
        User user = dto.toEntity();
        userRepository.insertUser(user);
    }

    private boolean isDuplicatedUser(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

}
