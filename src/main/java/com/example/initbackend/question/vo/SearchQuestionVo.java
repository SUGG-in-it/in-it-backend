package com.example.initbackend.question.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchQuestionVo {

    private Long questionId;

    private Long userId;

    private String title;

    private String content;


    private String nickname;

    private String level;

    private String tagList;

    private Integer point;

    private String type;

    private Timestamp createDate;

    private Timestamp updateDate;

    private Integer answerCount;

}
