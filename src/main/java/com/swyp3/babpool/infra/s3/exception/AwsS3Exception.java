package com.swyp3.babpool.infra.s3.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AwsS3Exception extends RuntimeException{

    private final AwsS3ErrorCode errorCode;
    private final String message;
}
