package com.swyp3.babpool.domain.review.api.request;

import com.swyp3.babpool.domain.review.domain.ReviewRateType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class ReviewCreateRequest {

    @Null
    private Long reviewId;
    private Long reviewerUserId;
    @NotNull
    private Long appointmentId;
    private ReviewRateType reviewRate;
    private String reviewComment;

    @Builder
    public ReviewCreateRequest(Long reviewId, Long reviewerUserId, Long appointmentId, ReviewRateType reviewRate, String reviewComment) {
        this.reviewId = reviewId;
        this.reviewerUserId = reviewerUserId;
        this.appointmentId = appointmentId;
        this.reviewRate = reviewRate;
        this.reviewComment = reviewComment;
    }

    public ReviewCreateRequest setReviewerUserId(Long reviewerUserId) {
        this.reviewerUserId = reviewerUserId;
        return this;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }
}
