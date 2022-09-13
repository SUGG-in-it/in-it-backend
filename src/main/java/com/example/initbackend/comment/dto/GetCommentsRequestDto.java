package com.example.initbackend.comment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class GetCommentsRequestDto {

    private Integer page;
    private Integer count;

    @Builder
    public GetCommentsRequestDto(Integer page, Integer count){
        this.page = page;
        this.count = count;
    }
}
