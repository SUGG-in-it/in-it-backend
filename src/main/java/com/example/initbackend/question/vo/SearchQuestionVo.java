package com.example.initbackend.question.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchQuestionVo {
    private Long questionId;
    private String title;
    private String content;
    private Timestamp updatedAt;
    private String type;
}
