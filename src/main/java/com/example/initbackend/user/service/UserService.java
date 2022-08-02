package com.example.initbackend.user.service;

import com.example.initbackend.user.controller.dto.ChangePasswordDto;
import com.example.initbackend.user.controller.dto.DuplicatedUserRequestDto;
import com.example.initbackend.user.controller.dto.JoinRequestDto;
import com.example.initbackend.user.controller.dto.UpdateUserPasswordRequestDto;
import com.example.initbackend.user.domain.User;
import com.example.initbackend.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

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
        userRepository.save(user);
    }
    public void duplicatedEmail(DuplicatedUserRequestDto duplicatedUserRequestDto){
        if (isDuplicatedUser(duplicatedUserRequestDto.getEmail())){
            throw new IllegalArgumentException("Duplicated User");
        }
    }

    public void changePassword(ChangePasswordDto changePasswordDto){
        String email = changePasswordDto.toEntity().getEmail();
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent()) {
            throw new EntityNotFoundException(
                    "User not present in the database");
        }

        User user = optionalUser.get();
        user.setPassword(changePasswordDto.toEntity().getPassword());
        userRepository.save(user);
    }

    private boolean isDuplicatedUser(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

//    public void updateUser(long id, UpdateUserPasswordRequestDto updateUserPasswordRequestDto){
//        User user = updateUserPasswordRequestDto.toEntity(getPassword());
//        userRepository.updateUser(user);
//    }
}
