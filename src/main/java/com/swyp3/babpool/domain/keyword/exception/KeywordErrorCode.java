package com.swyp3.babpool.domain.keyword.exception;

import com.swyp3.babpool.global.common.exception.errorcode.CustomErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum KeywordErrorCode implements CustomErrorCode {

    KEYWORD_NOT_FOUND(HttpStatus.NOT_FOUND, "키워드 정보가 존재하지 않습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;

}
