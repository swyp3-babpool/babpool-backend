package com.swyp3.babpool.domain.reject.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RejectException extends RuntimeException{

    private final RejectErrorCode errorCode;
    private final String message;

}
