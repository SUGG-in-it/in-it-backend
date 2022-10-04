package com.example.initbackend.tag.service;


import com.example.initbackend.tag.domain.Tag;
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

    public GetTagListResponseVo getTagList(){

        List<Tag> optionalTag = tagRepository.findAllByOrderByTag();
        List<String> tags = new ArrayList<>();
        for(Tag t :optionalTag){
            tags.add(t.getTag());
        }
        GetTagListResponseVo getTagListResponseVo = new GetTagListResponseVo(tags);
        return getTagListResponseVo;
    }
}
