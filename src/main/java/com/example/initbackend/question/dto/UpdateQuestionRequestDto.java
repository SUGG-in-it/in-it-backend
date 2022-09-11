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
    private static String title;

    @NotBlank
    private static String content;
    @NotBlank
    private static String tagList;

    @NotBlank(message = "이메일을 입력해주세요")
    @Email(message = "이메일 양식 오류")
    private String email;

    private static Integer point;



    @Builder
    public UpdateQuestionRequestDto(String title, String content, String tagList, String email, Integer point) {
        this.title = title;
        this.content = content;
        this.tagList = tagList;
        this.email = email;
        this.point = point;
    }

    public static Question toEntity(Long userId){
        return Question.builder()
                .title(title)
                .content(content)
                .tag_list(tagList)
                .user_id(userId)
                .point(point)
                .build();
    }
}
