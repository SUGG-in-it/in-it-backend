package com.example.initbackend.comment.service;

import com.example.initbackend.answer.domain.Answer;
import com.example.initbackend.answer.repository.AnswerRepository;
import com.example.initbackend.comment.domain.Comment;
import com.example.initbackend.comment.dto.RegisterCommentRequestDto;
import com.example.initbackend.comment.repository.CommentRepository;
import com.example.initbackend.comment.vo.CommentVo;
import com.example.initbackend.comment.vo.GetCommentsResponseVo;
import com.example.initbackend.comment.vo.GetCommentsTotalPageNumResponseVo;
import com.example.initbackend.comment.vo.GetUserCommentsTotalPageNumResponseVo;
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
import javax.transaction.Transactional;
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


    @Transactional
    public void registerComment(HttpServletRequest servletRequest, RegisterCommentRequestDto registerCommentRequestDto) {
        String token = jwtTokenProvider.resolveAccessToken(servletRequest);
        Long userId = jwtUtil.getPayloadByToken(token);

        userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.DATA_NOT_FOUND));
        answerRepository.findById(registerCommentRequestDto.getAnswerId()).orElseThrow(() -> new CustomException(ErrorCode.DATA_NOT_FOUND));

        Comment comment = registerCommentRequestDto.toEntity().builder()
                .userId(userId)
                .build();

        commentRepository.save(comment);

    }

    @Transactional
    public GetCommentsResponseVo getComments(Pageable pageable, Long answerId) {
        List<CommentVo> comments = new ArrayList<>();
        Page<Comment> optionalComments = commentRepository.findAllByAnswerId(answerId, pageable);

        for (Comment comment : optionalComments) {
            User user = userRepository.findById(comment.getUserId())
                    .orElseThrow(()->new CustomException(ErrorCode.DATA_NOT_FOUND));

            CommentVo commentVo = new CommentVo(
                    comment.getUserId(),
                    comment.getAnswerId(),
                    comment.getId(),
                    user.getNickname(),
                    comment.getContent(),
                    comment.getUpdatedAt()
            );
            comments.add(commentVo);
        };

        return new GetCommentsResponseVo(comments);
    }

    @Transactional
    public void deleteComment(HttpServletRequest servletRequest, Long commentId) {
        String token = jwtTokenProvider.resolveAccessToken(servletRequest);
        Long userId = jwtUtil.getPayloadByToken(token);

        userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.DATA_NOT_FOUND));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new CustomException(ErrorCode.DATA_NOT_FOUND));

        if(!userId.equals(comment.getUserId())){
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        commentRepository.deleteById(commentId);
    }

    @Transactional
    public GetCommentsTotalPageNumResponseVo getCommentsTotalPageNum(Pageable pageable, Long answerId) {
        Page<Comment> optionalComments = commentRepository.findAllByAnswerId(answerId, pageable);

        return new GetCommentsTotalPageNumResponseVo(optionalComments.getTotalPages());
    }

    @Transactional
    public GetUserCommentsTotalPageNumResponseVo getUserCommentsTotalPageNum(HttpServletRequest request, Pageable pageable){
        String token = jwtTokenProvider.resolveAccessToken(request);
        Long userId = JwtUtil.getPayloadByToken(token);

        Page<Comment> comments = commentRepository.findAllByUserIdOrderByCreatedAtDesc(userId, pageable);

        return new GetUserCommentsTotalPageNumResponseVo(comments.getTotalPages());
    }

    @Transactional
    public GetCommentsResponseVo getManagedComments(HttpServletRequest servletRequest, Pageable pageable) {
        String token = jwtTokenProvider.resolveAccessToken(servletRequest);
        Long userId = JwtUtil.getPayloadByToken(token);

        List<CommentVo> comments = new ArrayList<>();
        Page<Comment> optionalComments = commentRepository.findAllByUserId(userId, pageable);

        for (Comment comment : optionalComments) {
            User user = userRepository.findById(comment.getUserId())
                    .orElseThrow(()->new CustomException(ErrorCode.DATA_NOT_FOUND));
            CommentVo commentVo = new CommentVo(
                    comment.getUserId(),
                    comment.getAnswerId(),
                    comment.getId(),
                    user.getNickname(),
                    comment.getContent(),
                    comment.getUpdatedAt()
            );
            comments.add(commentVo);
        }

        return new GetCommentsResponseVo(comments);
    }

}
