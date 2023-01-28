package com.example.initbackend.tag.service;


import com.example.initbackend.global.handler.CustomException;
import com.example.initbackend.global.response.ErrorCode;
import com.example.initbackend.questionTag.repository.QuestionTagRepository;
import com.example.initbackend.tag.domain.Tag;
import com.example.initbackend.tag.dto.GetTagListRequestDto;
import com.example.initbackend.tag.repository.TagRepository;
import com.example.initbackend.tag.vo.GetTagListResponseVo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final QuestionTagRepository questionTagRepository;

    public GetTagListResponseVo getTagList(GetTagListRequestDto getTagListRequestDto){
        String type = getTagListRequestDto.getType();
        List<String> tags = new ArrayList<>();
        List<Tag> specialTag = new ArrayList<>();
        if (type.equals("all")){
            specialTag = tagRepository.findAllByOrderByTag();
        } else if (type.equals("popular")){
            List<Long> popularTagIds = questionTagRepository.countTagIdByOrderByCount();
            List<Tag> popularTags = tagRepository.findTop10ByIdIn(popularTagIds);
        } else {
            throw new CustomException(ErrorCode.TAG_TYPE_ERROR);
        }

        for(Tag t : specialTag){
            tags.add(t.getTag());
        }

        GetTagListResponseVo getTagListResponseVo = new GetTagListResponseVo(tags);
        return getTagListResponseVo;
    }
}
