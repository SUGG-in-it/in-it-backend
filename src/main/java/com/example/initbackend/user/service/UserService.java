package com.example.initbackend.user.service;

import com.example.initbackend.user.controller.dto.DuplicatedUserRequestDto;
import com.example.initbackend.user.controller.dto.JoinRequestDto;
import com.example.initbackend.user.domain.User;
import com.example.initbackend.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void join(JoinRequestDto joinRequestDto){
        if (isDuplicatedUser(joinRequestDto.getEmail())){
            throw new IllegalArgumentException("Duplicated User");
        }
        User user = joinRequestDto.toEntity();
        userRepository.insertUser(user);
    }
    public void duplicatedEmail(DuplicatedUserRequestDto duplicatedUserRequestDto){
        if (isDuplicatedUser(duplicatedUserRequestDto.getEmail())){
            throw new IllegalArgumentException("Duplicated User");
        }

    }

    private boolean isDuplicatedUser(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

}
