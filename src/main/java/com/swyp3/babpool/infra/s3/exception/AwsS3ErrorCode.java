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
    AWS_S3_INVALID_FILE_TYPE(HttpStatus.BAD_REQUEST, "Invalid file type. Only JPEG and PNG are supported."),
    AWS_S3_FILE_TOO_LARGE(HttpStatus.BAD_REQUEST, "File size is too large. Maximum allowed size is 5MB."),
    AWS_S3_IMAGE_DELETE_FAIL(HttpStatus.BAD_REQUEST, "Aws s3 image delete fail.");

    private final HttpStatus httpStatus;
    private final String message;
}
