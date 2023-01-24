package com.example.initbackend.comment.dto;

import com.example.initbackend.comment.domain.Comment;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class RegisterCommentRequestDto {
    private Long answerId;
    private String content;

    @Builder
    public RegisterCommentRequestDto(Long answerId, String content){
        this.answerId = answerId;
        this.content = content;
    }

    public Comment toEntity() {
        return Comment.builder()
                .answerId(answerId)
                .content(content)
                .build();
    }
}
