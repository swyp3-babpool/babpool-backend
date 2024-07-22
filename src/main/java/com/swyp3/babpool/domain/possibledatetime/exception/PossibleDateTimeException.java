package com.swyp3.babpool.domain.possibledatetime.exception;

import com.swyp3.babpool.domain.possibledatetime.exception.errorcode.PossibleDateTimeErrorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PossibleDateTimeException extends RuntimeException{

    private final PossibleDateTimeErrorCode errorCode;
    private final String message;
}
