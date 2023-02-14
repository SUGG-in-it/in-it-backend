package com.example.initbackend.user.service;

import com.example.initbackend.global.handler.CustomException;
import com.example.initbackend.global.response.ErrorCode;
import com.example.initbackend.user.dto.JoinRequestDto;
import com.example.initbackend.user.repository.UserRepository;
import com.example.initbackend.user.domain.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class UserServiceTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;

    @BeforeEach
    public void setUser() {
        User user = User.builder()
                .email("test@gmail.com")
                .password("test123")
                .nickname("test")
                .build();

        userRepository.save(user);
    }

    @AfterEach
    public void resetUser() {
        userRepository.deleteByEmail("test@gmail.com");
    }

    @Test
    @DisplayName("join API :: duplicatedEmail")
    void join_duplicatedEmail_conflict() {
        // given
        JoinRequestDto joinRequestDto = JoinRequestDto.builder()
                .email("test@gmail.com")
                .password("test123")
                .nickname("test2")
                .build();
        // when, then
//        userService.join(joinRequestDto);

//        assertThatThrownBy(() -> userService.join(joinRequestDto))
//                .isInstanceOf(CustomException.class)
//                .hasMessage("Duplicated data exist");
//        try {
//            userService.join(joinRequestDto);
//        } catch (CustomException e) {
//            Assertions.assertEquals("");
//        }
    }



//
//    @Test
//    void join2() {
//        // 이메일 겹치는 경우
//        // 겹치지 않는 경우
//    }
//
//    @Test
//    void duplicatedEmail() {
//    }
//
//    @Test
//    void duplicatedNickname() {
//    }
//
//    @Test
//    void changePassword() {
//    }
//
//    @Test
//    void login() {
//    }
//
//    @Test
//    void loadUserByUsername() {
//    }
//
//    @Test
//    void getUserByNickname() {
//    }
//
//    @Test
//    void updateUser() {
//    }
//
//    @Test
//    void getUserRepository() {
//    }
//
//    @Test
//    void getTokenRepository() {
//    }
//
//    @Test
//    void getAuthenticationManagerBuilder() {
//    }
//
//    @Test
//    void getJwtTokenProvider() {
//    }
}