package com.example.initbackend.comment.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentVo {
    private Long answerId;
    private Long commentId;
    private String nickname;
    private String content;
    private Timestamp updatedAt;
}
