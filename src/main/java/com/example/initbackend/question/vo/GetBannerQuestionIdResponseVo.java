package com.example.initbackend.question.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetBannerQuestionIdResponseVo {
    @JsonProperty
    private Long questionId;
    public GetBannerQuestionIdResponseVo(Long questionId) {
        this.questionId = questionId;
    }
}