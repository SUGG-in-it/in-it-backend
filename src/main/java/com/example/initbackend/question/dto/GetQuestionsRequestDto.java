package com.example.initbackend.question.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@NoArgsConstructor
public class GetQuestionsRequestDto {
    private String type;
    private Integer page;
    private Integer count;

    @Builder
    public GetQuestionsRequestDto(String type, Integer count, Integer page) {

        this.type = type;
        this.count = count;
        this.page = page;
    }
}
