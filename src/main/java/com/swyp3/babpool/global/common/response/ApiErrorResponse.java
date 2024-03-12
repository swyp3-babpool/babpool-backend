package com.swyp3.babpool.global.common.response;

import com.swyp3.babpool.global.common.exception.errorcode.CustomErrorCode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ApiErrorResponse {

    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int statusCode;
    private final String statusDescription;
    private final String message;

    public ApiErrorResponse(int statusCode, String statusDescription, String message) {
        this.statusCode = statusCode;
        this.statusDescription = statusDescription;
        this.message = message;
    }

    public static ApiErrorResponse of(CustomErrorCode errorCode) {
        return new ApiErrorResponse(
                errorCode.getHttpStatus().value(),
                errorCode.getHttpStatus().name(),
                errorCode.getMessage()
        );
    }

    public static ApiErrorResponse of(CustomErrorCode errorCode, String message) {
        return new ApiErrorResponse(
                errorCode.getHttpStatus().value(),
                errorCode.getHttpStatus().name(),
                message
        );
    }

}
