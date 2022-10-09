package com.example.initbackend.global.response;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    // Member
    NOT_FOUND_MEMBER(HttpStatus.BAD_REQUEST, "MEMBER-0001", "Member not found"),
    EXISTING_MEMBER(HttpStatus.BAD_REQUEST, "MEMBER-0002", "Member that already exists"),
    TAGLIST_TOO_LONG(HttpStatus.BAD_REQUEST, "QUESTION-0001", "TagList too long"),

    // Post
    NOT_FOUND_POST(HttpStatus.BAD_REQUEST, "POST-0001", "Post not found"),

    // File
    AWS_S3_UPLOAD_FAIL(HttpStatus.BAD_REQUEST, "FILE-0001", "AWS S3 File upload fail"),
    AWS_S3_UPLOAD_VALID(HttpStatus.BAD_REQUEST, "FILE-0002", "File validation"),

    // Role
    NOT_HAVE_PERMISSION(HttpStatus.BAD_REQUEST, "ROLE-0001", "You do not have permission"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "ROLE-0002", "Unauthorized"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "ROLE-0003", "Forbidden"),
    CONFLICT(HttpStatus.CONFLICT, "ROLE-0004", "Duplicated data exist"),
    DATA_NOT_FOUND(HttpStatus.NOT_FOUND, "ROLE-0005", "Data Not Found"),
    UNAUTHORIZED_PASSWORD(HttpStatus.UNAUTHORIZED, "ROLE_0006", "Incorrect password"),
    StatusGone(HttpStatus.GONE, "ROLE_0007", "Gone"),


    //JWT
    JWT_ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "TOKEN-0001", "Access token has expired"),
    JWT_REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "TOKEN-0002", "Refresh token has expired"),
    JWT_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "TOKEN-0003", "Token unauthorized"),


    // CERTIFICATION
    CERTIFICATION_CODE_EXPIRED(HttpStatus.UNAUTHORIZED, "CERTIFICATION-0001", "Certification code has expired"),
    UNAUTHORIZED_CERTIFICATION_CODE(HttpStatus.UNAUTHORIZED, "CERTIFICATION-0002", "Incorrect certification code");

    HttpStatus status;
    String code;
    String message;

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
