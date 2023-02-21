package com.example.initbackend.answer.dto;

import com.example.initbackend.answer.domain.Answer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateAnswerRequestDto {

    private String content;

    @Builder
    public UpdateAnswerRequestDto(String content) {
        this.content = content;
    }
}
