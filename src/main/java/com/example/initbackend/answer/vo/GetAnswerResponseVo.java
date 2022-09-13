package com.example.initbackend.answer.vo;


import com.example.initbackend.answer.domain.Answer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetAnswerResponseVo {
    private List<Answer> answerList;
}
