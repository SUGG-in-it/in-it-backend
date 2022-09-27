package com.example.initbackend.question.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchQuestionsResponseVo {
    private List<SearchQuestionVo> searchQuestionList;

}
