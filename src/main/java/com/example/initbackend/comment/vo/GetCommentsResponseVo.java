package com.example.initbackend.comment.vo;

import com.example.initbackend.comment.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetCommentsResponseVo {
    private List<CommentVo> comments;
}

