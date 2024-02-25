package com.swyp3.babpool.global.util.jwt.exception.errorcode;

import com.swyp3.babpool.global.common.exception.errorcode.CustomErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum JwtExceptionErrorCode implements CustomErrorCode {

    REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "refresh token not found"),
    REFRESH_TOKEN_NOT_SAME_USER(HttpStatus.UNAUTHORIZED, "refresh token not same user");

    private final HttpStatus httpStatus;
    private final String message;
}
