package com.example.initbackend.global.handler;

import com.example.initbackend.global.response.ErrorCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private ErrorCode error;

    public ErrorCode getErrorCode() {
        return error;
    }

    public CustomException(ErrorCode e) {
        super(e.getMessage());
        this.error = e;
    }
}