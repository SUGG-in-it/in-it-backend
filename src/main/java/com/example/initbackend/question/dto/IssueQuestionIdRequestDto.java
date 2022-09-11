package com.example.initbackend.question.dto;

import com.example.initbackend.question.domain.Question;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class IssueQuestionIdRequestDto {

    private Long userId;

    @Builder
    public IssueQuestionIdRequestDto(Long userId) {
        this.userId = userId;
    }

    public static Question toEntity(Long userId){
        return Question.builder()
                .user_id(userId)
                .build();
    }
}
