package com.example.initbackend.comment.dto;

import com.example.initbackend.comment.domain.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class RegisterCommentRequestDto {

    private Long userId;
    private Long answerId;
    private String content;

    public Comment toEntity() {
        return Comment.builder()
                .content(content)
                .answerId(answerId)
                .userId(userId)
                .build();
    }
}
