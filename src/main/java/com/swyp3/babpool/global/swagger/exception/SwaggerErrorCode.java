package com.swyp3.babpool.global.swagger.exception;

import com.swyp3.babpool.global.common.exception.errorcode.CustomErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SwaggerErrorCode implements CustomErrorCode {
    NOTFOUND_COOKIE(HttpStatus.NOT_FOUND, "쿠키를 찾을 수 없습니다."),
    NOTFOUND_TOKEN_IN_COOKIE(HttpStatus.NOT_FOUND, "쿠키에서 토큰을 찾을 수 없습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다."),
    INVALID_TOKEN_EMPTY(HttpStatus.UNAUTHORIZED, "토큰이 비어 있습니다."),
    INVALID_TOKEN_AUTHORITY(HttpStatus.UNAUTHORIZED, "토큰의 권한이 없습니다.");
    private final HttpStatus httpStatus;
    private final String message;
}
