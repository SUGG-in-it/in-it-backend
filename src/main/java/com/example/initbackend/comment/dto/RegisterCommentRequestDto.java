package com.example.initbackend.comment.dto;

import com.example.initbackend.comment.domain.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class RegisterCommentRequestDto {

    Long userId;
    Long answerId;
    String content;



    public Comment toEntity() {
        return Comment.builder()
                .content(content)
                .answer_id(answerId)
                .user_id(userId)
                .build();
    }
}
