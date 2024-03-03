package com.swyp3.babpool.global.jwt.exception;

import com.swyp3.babpool.global.jwt.exception.errorcode.JwtExceptionErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BabpoolJwtException extends RuntimeException{

    private final JwtExceptionErrorCode jwtExceptionErrorCode;
    private final String message;
}
