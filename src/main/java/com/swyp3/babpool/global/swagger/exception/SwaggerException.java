package com.swyp3.babpool.global.swagger.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SwaggerException extends RuntimeException{

    private final SwaggerErrorCode errorCode;
    private final String message;
}
