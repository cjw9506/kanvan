package com.kanvan.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // 예시)
    // RECRUITMENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "R001", "해당하는 채용공고가 없습니다."),

    //user
    USER_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "U001", "이미 계정이 존재합니다."),
    USER_PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "U002", "비밀번호가 일치하지 않습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U003", "해당 계정은 존재하지 않습니다."),

    //team
    TEAM_IS_EXIST(HttpStatus.BAD_REQUEST, "T001", "이미 팀명이 존재합니다."),
    TEAM_NOT_FOUND(HttpStatus.NOT_FOUND, "T002", "해당 팀이 존재하지 않습니다."),

    //member
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "M001", "해당 멤버는 존재하지 않습니다."),
    MEMBER_NOT_LEADER(HttpStatus.BAD_REQUEST, "M002", "해당 멤버는 리더가 아닙니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

}
