package com.example.initbackend.answer.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ManagedAnswerVo {

    private Long id;
    private Long questionId;
    private String questionTitle;
    private Long userId;
    private String content;
    private boolean isSelected;
    private Timestamp createDate;
    private Timestamp updateDate;
}
