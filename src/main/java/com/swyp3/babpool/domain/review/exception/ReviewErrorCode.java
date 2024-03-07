package com.swyp3.babpool.domain.review.exception;

import com.swyp3.babpool.global.common.exception.errorcode.CustomErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReviewErrorCode implements CustomErrorCode {

    NOT_FOUND_REVIEW(HttpStatus.NOT_FOUND, "There is no review for the requested profile."),
    NOT_VALID_REVIEW_REQUEST(HttpStatus.BAD_REQUEST, "Not valid review request."),
    REVIEW_CREATE_REQUEST_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create review."),
    REVIEW_UPDATE_REQUEST_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update review."),
    ALREADY_EXIST_REVIEW(HttpStatus.BAD_REQUEST, "Review already exists."),
    REVIEW_LIST_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while retrieving the review list."),
    ;
    private final HttpStatus httpStatus;
    private final String message;
}
