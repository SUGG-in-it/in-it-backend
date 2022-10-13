package com.example.initbackend.answer.vo;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetManagedAnswersResponseVo {
    private List<ManagedAnswerVo> managedAnswers;
}
