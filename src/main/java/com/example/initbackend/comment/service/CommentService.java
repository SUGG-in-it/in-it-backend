package com.example.initbackend.comment.service;

import com.example.initbackend.answer.domain.Answer;
import com.example.initbackend.answer.repository.AnswerRepository;
import com.example.initbackend.comment.domain.Comment;
import com.example.initbackend.comment.dto.GetCommentsRequestDto;
import com.example.initbackend.comment.dto.RegisterCommentRequestDto;
import com.example.initbackend.comment.repository.CommentRepository;
import com.example.initbackend.comment.vo.CommentVo;
import com.example.initbackend.comment.vo.GetCommentsResponseVo;
import com.example.initbackend.comment.vo.GetCommentsTotalPageNumResponseVo;
import com.example.initbackend.global.handler.CustomException;
import com.example.initbackend.global.jwt.JwtTokenProvider;
import com.example.initbackend.global.jwt.JwtUtil;
import com.example.initbackend.global.response.ErrorCode;
import com.example.initbackend.user.domain.User;
import com.example.initbackend.user.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Getter
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final AnswerRepository answerRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtUtil jwtUtil;


    public void registerComment(HttpServletRequest servletRequest, RegisterCommentRequestDto registerCommentRequestDto) {
        String token = jwtTokenProvider.resolveAccessToken(servletRequest);
        Long userId = jwtUtil.getPayloadByToken(token);

        Optional<User> optionalUser = userRepository.findById(userId);
        Optional<Answer> optionalAnswer = answerRepository.findById(registerCommentRequestDto.getAnswerId());

        if (!optionalUser.isPresent() || !optionalAnswer.isPresent()) {
            throw new CustomException(ErrorCode.DATA_NOT_FOUND);
        }

        Comment comment = registerCommentRequestDto.toEntity();
        comment.setUserId(userId);

        commentRepository.save(comment);

    }

    public GetCommentsResponseVo getComments(Pageable pageable, Long answerId) {
        List<CommentVo> comments = new ArrayList<>();
        Page<Comment> optionalComments = commentRepository.findAllByAnswerId(answerId, pageable);

        optionalComments.stream().forEach(
                comment -> {
                    Optional<User> optionalUser = userRepository.findById(comment.getUserId());
                    CommentVo commentVo = new CommentVo(
                            comment.getAnswerId(),
                            comment.getId(),
                            optionalUser.get().getNickname(),
                            comment.getContent(),
                            comment.getUpdatedAt()
                    );
                    comments.add(commentVo);
                }
        );

        return new GetCommentsResponseVo(comments);
    }

    public void deleteComment(HttpServletRequest servletRequest, Long commentId) {
        String token = jwtTokenProvider.resolveAccessToken(servletRequest);
        Long userId = jwtUtil.getPayloadByToken(token);

        Optional<User> optionalUser = userRepository.findById(userId);
        Optional<Comment> optionalComment = commentRepository.findById(commentId);

        if (!optionalUser.isPresent() || !optionalComment.isPresent()) {
            throw new CustomException(ErrorCode.DATA_NOT_FOUND);
        }

        if(!userId.equals(optionalComment.get().getUserId())){
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        commentRepository.deleteById(commentId);
    }

    public GetCommentsTotalPageNumResponseVo getCommentsTotalPageNum(Pageable pageable, Long answerId) {
        Page<Comment> optionalComments = commentRepository.findAllByAnswerId(answerId, pageable);

        GetCommentsTotalPageNumResponseVo getCommentsTotalPageNumResponse = new GetCommentsTotalPageNumResponseVo(optionalComments.getTotalPages());

        return getCommentsTotalPageNumResponse;
    }

    public GetCommentsResponseVo getManagedComments(HttpServletRequest servletRequest, Pageable pageable) {
        String token = jwtTokenProvider.resolveAccessToken(servletRequest);
        Long userId = JwtUtil.getPayloadByToken(token);

        List<CommentVo> comments = new ArrayList<>();
        Page<Comment> optionalComments = commentRepository.findAllByUserId(userId, pageable);

        optionalComments.stream().forEach(
                comment -> {
                    Optional<User> optionalUser = userRepository.findById(comment.getUserId());
                    CommentVo commentVo = new CommentVo(
                            comment.getAnswerId(),
                            comment.getId(),
                            optionalUser.get().getNickname(),
                            comment.getContent(),
                            comment.getUpdatedAt()
                    );
                    comments.add(commentVo);
                }
        );

        return new GetCommentsResponseVo(comments);
    }

}
