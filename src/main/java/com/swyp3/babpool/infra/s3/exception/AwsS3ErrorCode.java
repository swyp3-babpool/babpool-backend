package com.swyp3.babpool.infra.s3.exception;


import com.swyp3.babpool.global.common.exception.errorcode.CustomErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
//import org.apache.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AwsS3ErrorCode implements CustomErrorCode {

    AWS_S3_IMAGE_UPLOAD_FAIL(HttpStatus.FAILED_DEPENDENCY, "Aws s3 image upload fail."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
