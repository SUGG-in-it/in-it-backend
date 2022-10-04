package com.example.initbackend.tag.vo;

import com.example.initbackend.tag.domain.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetTagListResponseVo {
    private List<String> tags;
}
