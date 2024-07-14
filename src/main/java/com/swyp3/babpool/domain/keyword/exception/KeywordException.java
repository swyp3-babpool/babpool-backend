package com.swyp3.babpool.domain.keyword.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class KeywordException extends RuntimeException{

        private final KeywordErrorCode errorCode;
        private final String message;
}
