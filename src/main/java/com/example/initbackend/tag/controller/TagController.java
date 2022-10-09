package com.example.initbackend.tag.controller;

import com.example.initbackend.global.response.StatusEnum;
import com.example.initbackend.global.response.SuccessResponse;
import com.example.initbackend.tag.dto.GetTagListRequestDto;
import com.example.initbackend.tag.service.TagService;
import com.example.initbackend.tag.vo.GetTagListResponseVo;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/tags")
public class TagController {

    private final TagService tagService;

    @GetMapping
    public SuccessResponse getTagList(@RequestParam(value = "type") GetTagListRequestDto requestDto){
        GetTagListResponseVo getAnswerResponseVo = tagService.getTagList(requestDto);

        SuccessResponse res = SuccessResponse.builder()
                .status(StatusEnum.OK)
                .data(getAnswerResponseVo)
                .build();
        return res;
    }
}
