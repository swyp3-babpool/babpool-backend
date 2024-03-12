package com.swyp3.babpool.global.common.response;

import com.swyp3.babpool.global.common.exception.errorcode.CustomErrorCode;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
public class ApiErrorResponse {

    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int statusCode;
    private final String statusDescription;
    private final String message;

    @Builder
    public ApiErrorResponse(int statusCode, String statusDescription, String message) {
        this.statusCode = statusCode;
        this.statusDescription = statusDescription;
        this.message = message;
    }

    public static ApiErrorResponse of(CustomErrorCode customErrorCode) {
        return ApiErrorResponse.of(customErrorCode, customErrorCode.getMessage());
    }

    public static ApiErrorResponse of(CustomErrorCode customErrorCode, String message) {
        return ApiErrorResponse.of(customErrorCode.getHttpStatus(), message);
    }

    public static ApiErrorResponse of(HttpStatus httpStatus, String message) {
        return new ApiErrorResponse(
                httpStatus.value(),
                httpStatus.name(),
                message
        );
    }

}
