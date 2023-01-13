package com.example.initbackend.global.response;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    // Member
    NOT_FOUND_MEMBER(HttpStatus.BAD_REQUEST,  "Member not found"),
    EXISTING_MEMBER(HttpStatus.BAD_REQUEST,  "Member that already exists"),
    TAGLIST_TOO_LONG(HttpStatus.BAD_REQUEST, "TagList too long"),
    TAG_TYPE_ERROR(HttpStatus.BAD_REQUEST,  "Tag type error"),

    // Post
    NOT_FOUND_POST(HttpStatus.BAD_REQUEST,  "Post not found"),

    // File
    AWS_S3_UPLOAD_FAIL(HttpStatus.BAD_REQUEST,  "AWS S3 File upload fail"),
    AWS_S3_UPLOAD_VALID(HttpStatus.BAD_REQUEST, "File validation"),

    // Role
    NOT_HAVE_PERMISSION(HttpStatus.BAD_REQUEST, "You do not have permission"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED,  "Unauthorized"),
    FORBIDDEN(HttpStatus.FORBIDDEN,  "Forbidden"),
    CONFLICT(HttpStatus.CONFLICT, "Duplicated data exist"),
    DATA_NOT_FOUND(HttpStatus.NOT_FOUND,  "Data Not Found"),
    UNAUTHORIZED_PASSWORD(HttpStatus.UNAUTHORIZED, "Incorrect password"),
    StatusGone(HttpStatus.GONE,  "Gone"),


    //JWT
    JWT_ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "Access token has expired"),
    JWT_REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED,  "Refresh token has expired"),
    JWT_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Token unauthorized"),


    // CERTIFICATION
    CERTIFICATION_CODE_EXPIRED(HttpStatus.UNAUTHORIZED, "Certification code has expired"),
    UNAUTHORIZED_CERTIFICATION_CODE(HttpStatus.UNAUTHORIZED, "Incorrect certification code");

    HttpStatus status;
    String message;

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
