package com.example.initbackend.question.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor
public class GetQuestionResponseVo {
    private Long questionId;
    private String title;
    private String content;

    private Long userId;
    private String nickname;
    private String level;
    private String tagList;

    private String type;

    private Timestamp createDate;

    private Timestamp updateDate;

    private Integer point;

    private Long answerCount;

    private Long likeCount;

    private Boolean isLike;

    public GetQuestionResponseVo(Long questionId, Long userId, String title, String content, String nickname, String level, Integer point, String tagList, String type, Timestamp createDate, Timestamp updateDate, Long answerCount, Long likeCount, Boolean isLike) {
        this.questionId = questionId;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.nickname = nickname;
        this.level = level;
        this.tagList = tagList;
        this.point = point;
        this.type = type;
        this.createDate = createDate;
        this.updateDate = updateDate;
        this.answerCount = answerCount;
        this.likeCount = likeCount;
        this.isLike = isLike;
    }
}