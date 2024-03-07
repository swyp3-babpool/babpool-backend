package com.swyp3.babpool.domain.review.api.request;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class ReviewUpdateRequest {

    private Long reviewId;
    private Long reviewerUserId;
    private Long targetAppointmentId;
    private String rateType;
    private String reviewContent;

    public ReviewUpdateRequest(Long reviewId, Long reviewerUserId, Long targetAppointmentId, String rateType, String reviewContent) {
        this.reviewId = reviewId;
        this.reviewerUserId = reviewerUserId;
        this.targetAppointmentId = targetAppointmentId;
        this.rateType = rateType;
        this.reviewContent = reviewContent;
    }

    public ReviewUpdateRequest setReviewerUserId(Long reviewerUserId) {
        this.reviewerUserId = reviewerUserId;
        return this;
    }
}
