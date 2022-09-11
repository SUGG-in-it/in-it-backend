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

    @NotBlank(message = "이메일을 입력해주세요")
    @Email(message = "이메일 양식 오류")
    private String email;

    private  Integer point;



    @Builder
    public UpdateQuestionRequestDto(String title, String content, String tagList, String email, Integer point) {
        this.title = title;
        this.content = content;
        this.tagList = tagList;
        this.email = email;
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
