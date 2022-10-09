package com.example.initbackend.tag.service;


import com.example.initbackend.global.handler.CustomException;
import com.example.initbackend.global.response.ErrorCode;
import com.example.initbackend.question.vo.GetBannerQuestionIdResponseVo;
import com.example.initbackend.questionTag.domain.QuestionTag;
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

        if (type.equals("All")){
            List<Tag> optionalTag = tagRepository.findAllByOrderByTag();

            for(Tag t :optionalTag){
                tags.add(t.getTag());
            }

        } else if (type.equals("popular")){
            List<Long> popularTagIds = questionTagRepository.countTagIdByOrderByCount();
            System.out.println(popularTagIds);
            List<Tag> popularTags = tagRepository.findTop10ByIdIn(popularTagIds);
            System.out.println(popularTags);
            for(Tag t : popularTags){
                tags.add(t.getTag());
            }

            System.out.println(tags);

        } else {
            throw new CustomException(ErrorCode.TAG_TYPE_ERROR);
        }

        GetTagListResponseVo getTagListResponseVo = new GetTagListResponseVo(tags);
        return getTagListResponseVo;
    }
}
