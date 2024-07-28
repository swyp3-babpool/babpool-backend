package com.swyp3.babpool.domain.review.api.request;

import com.swyp3.babpool.domain.review.domain.ReviewRateType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class ReviewUpdateRequest {

    private Long reviewId;
    private Long reviewerUserId;
    private Long appointmentId;
    private ReviewRateType reviewRate;
    private String reviewComment;

    @Builder
    public ReviewUpdateRequest(Long reviewId, Long reviewerUserId, Long appointmentId, ReviewRateType reviewRate, String reviewComment) {
        this.reviewId = reviewId;
        this.reviewerUserId = reviewerUserId;
        this.appointmentId = appointmentId;
        this.reviewRate = reviewRate;
        this.reviewComment = reviewComment;
    }

    public ReviewUpdateRequest setReviewerUserId(Long reviewerUserId) {
        this.reviewerUserId = reviewerUserId;
        return this;
    }
}
