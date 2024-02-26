package com.swyp3.babpool.global.util.jwt.exception.errorcode;

import com.swyp3.babpool.global.common.exception.errorcode.CustomErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum JwtExceptionErrorCode implements CustomErrorCode {

    REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "refresh token not found"),
    REFRESH_TOKEN_NOT_SAME_USER(HttpStatus.UNAUTHORIZED, "refresh token not same user"),

    UNKNOWN_ERROR(HttpStatus.UNAUTHORIZED, "알 수 없는 에러"),
    NOT_FOUND_TOKEN(HttpStatus.UNAUTHORIZED, "Headers에서 토큰 형식의 값을 찾을 수 없음"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰"),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "기간이 만료된 토큰"),
    UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, "지원하지 않는 토큰");

    private final HttpStatus httpStatus;
    private final String message;
}
