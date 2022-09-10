package com.example.initbackend.comment.service;

import com.example.initbackend.answer.domain.Answer;
import com.example.initbackend.answer.repository.AnswerRepository;
import com.example.initbackend.comment.domain.Comment;
import com.example.initbackend.comment.dto.RegisterCommentRequestDto;
import com.example.initbackend.comment.repository.CommentRepository;
import com.example.initbackend.global.handler.CustomException;
import com.example.initbackend.global.jwt.dto.JwtResponseDto;
import com.example.initbackend.global.response.ErrorCode;
import com.example.initbackend.user.domain.User;
import com.example.initbackend.user.dto.*;
import com.example.initbackend.user.repository.UserRepository;
import com.example.initbackend.user.vo.GetProfileResponseVo;
import com.example.initbackend.user.vo.LoginResponseVo;
import com.example.initbackend.userToken.domain.UserToken;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Getter
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final AnswerRepository answerRepository;


    public void registerComment(RegisterCommentRequestDto registerCommentRequestDto) {
        Optional<User> optionalUser = userRepository.findById(registerCommentRequestDto.getUserId());
        Optional<Answer> optionalAnswer = answerRepository.findById(registerCommentRequestDto.getAnswerId());

        if (!optionalUser.isPresent() || !optionalAnswer.isPresent()){
            throw new CustomException(ErrorCode.DATA_NOT_FOUND);
        }

        Comment comment = registerCommentRequestDto.toEntity();

        commentRepository.save(comment);

    }

}
