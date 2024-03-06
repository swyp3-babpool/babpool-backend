package com.swyp3.babpool.domain.review.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReviewException extends RuntimeException{

    private final ReviewErrorCode errorCode;
    private final String message;
}
