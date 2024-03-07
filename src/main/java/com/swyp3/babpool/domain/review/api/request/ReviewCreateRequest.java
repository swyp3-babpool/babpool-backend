package com.swyp3.babpool.domain.review.api.request;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class ReviewCreateRequest {

    private Long reviewId;
    private Long reviewerUserId;
    private Long targetAppointmentId;
    private String rateType;
    private String reviewContent;

    @Builder
    public ReviewCreateRequest(Long reviewId, Long reviewerUserId, Long targetAppointmentId, String rateType, String reviewContent) {
        this.reviewId = reviewId;
        this.reviewerUserId = reviewerUserId;
        this.targetAppointmentId = targetAppointmentId;
        this.rateType = rateType;
        this.reviewContent = reviewContent;
    }

    public ReviewCreateRequest setReviewerUserId(Long reviewerUserId) {
        this.reviewerUserId = reviewerUserId;
        return this;
    }


}
