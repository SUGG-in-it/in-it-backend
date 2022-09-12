package com.example.initbackend.question.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor
public class GetQuestionResponseVo {
    @JsonProperty
    private Long questionId;
    @JsonProperty
    private String title;
    @JsonProperty
    private String content;
    @JsonProperty
    private String nickname;
    @JsonProperty
    private String level;
    @JsonProperty
    private String tagList;

    @JsonProperty
    private Timestamp createDate;

    @JsonProperty
    private Timestamp updateDate;

    @JsonProperty
    private Integer point;
    public GetQuestionResponseVo(Long questionId, String title, String content, String nickname, String level, Integer point, String tagList, Timestamp createDate, Timestamp updateDate) {
        this.questionId = questionId;
        this.title = title;
        this.content = content;
        this.nickname = nickname;
        this.level = level;
        this.tagList = tagList;
        this.point = point;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }
}