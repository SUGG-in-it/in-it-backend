package com.example.initbackend.answer.dto;

import com.example.initbackend.answer.domain.Answer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateAnswerRequestDto {

    private Long questionId;
    private Long userId;
    private String content;


    public Answer toEntity(){
        return Answer.builder()
                .questionId(questionId)
                .userId(userId)
                .content(content)
                .build();
    }
}
