package com.example.initbackend.answer.vo;


import com.example.initbackend.answer.domain.Answer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import java.sql.Timestamp;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetAnswerResponseVo {
    private Long answerId;

    private Long userId;

    private String nickname;

    private String content;

    private boolean isSelected;

    private Timestamp createDate;

    private Timestamp updateDate;
}
