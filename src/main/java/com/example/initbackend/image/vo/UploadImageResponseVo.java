package com.example.initbackend.image.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UploadImageResponseVo {
        @JsonProperty
        private String url;
        public UploadImageResponseVo(String url) {
            this.url = url;
        }
}