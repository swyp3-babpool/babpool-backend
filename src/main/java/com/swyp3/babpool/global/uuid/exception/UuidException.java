package com.swyp3.babpool.global.uuid.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UuidException extends RuntimeException{

    private final UuidErrorCode errorCode;
    private final String message;
}
