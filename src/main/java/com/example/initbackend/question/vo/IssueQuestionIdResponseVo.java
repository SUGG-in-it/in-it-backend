package com.example.initbackend.question.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class IssueQuestionIdResponseVo {
        @JsonProperty
        private Long questionId;
        public IssueQuestionIdResponseVo(Long questionId) {
            this.questionId = questionId;
        }
}