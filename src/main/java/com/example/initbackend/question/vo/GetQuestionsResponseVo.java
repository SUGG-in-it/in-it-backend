package com.example.initbackend.question.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class GetQuestionsResponseVo {
    @JsonProperty
    private List<GetQuestionResponseVo> questions;

    public GetQuestionsResponseVo(List<GetQuestionResponseVo> questions) {
        this.questions = questions;
    }
}