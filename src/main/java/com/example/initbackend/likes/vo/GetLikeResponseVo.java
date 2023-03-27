package com.example.initbackend.likes.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetLikeResponseVo {

    private boolean isLike;
    private long likeCount;
}
