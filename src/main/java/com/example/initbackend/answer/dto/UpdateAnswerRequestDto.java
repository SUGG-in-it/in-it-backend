package com.example.initbackend.answer.dto;

import com.example.initbackend.answer.domain.Answer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAnswerRequestDto {

    private Long questionId;
    private String content;


    public Answer toEntity(){
        return Answer.builder()
                .questionId(questionId)
                .content(content)
                .build();
    }
}
