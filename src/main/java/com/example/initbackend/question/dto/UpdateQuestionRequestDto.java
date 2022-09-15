package com.example.initbackend.question.dto;

import com.example.initbackend.question.domain.Question;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class UpdateQuestionRequestDto {

    @NotBlank
    private String title;

    @NotBlank
    private String content;
    @NotBlank
    private String tagList;

    private  Integer point;



    @Builder
    public UpdateQuestionRequestDto(String title, String content, String tagList, Integer point) {
        this.title = title;
        this.content = content;
        this.tagList = tagList;
        this.point = point;
    }

    public Question toEntity(){
        return Question.builder()
                .title(title)
                .content(content)
                .tagList(tagList)
                .point(point)
                .build();
    }

}
