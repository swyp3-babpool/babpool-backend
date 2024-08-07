package com.swyp3.babpool.domain.review.application.response;

import com.swyp3.babpool.domain.review.domain.Review;
import com.swyp3.babpool.domain.review.domain.ReviewRateType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class ReviewSaveResponse {

    private Long reviewId;
    private Long appointmentId;
    private ReviewRateType reviewRate;
    private String reviewComment;

    @Builder
    public ReviewSaveResponse(Long reviewId, Long appointmentId, ReviewRateType reviewRate, String reviewComment) {
        this.reviewId = reviewId;
        this.appointmentId = appointmentId;
        this.reviewRate = reviewRate;
        this.reviewComment = reviewComment;
    }

    public static ReviewSaveResponse of(Review review) {
        return ReviewSaveResponse.builder()
                .reviewId(review.getReviewId())
                .appointmentId(review.getAppointmentId())
                .reviewRate(review.getReviewRate())
                .reviewComment(review.getReviewComment())
                .build();
    }
}
