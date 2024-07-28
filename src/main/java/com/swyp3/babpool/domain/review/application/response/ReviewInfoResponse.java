package com.swyp3.babpool.domain.review.application.response;

import com.swyp3.babpool.domain.review.domain.Review;
import com.swyp3.babpool.domain.review.domain.ReviewRateType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
public class ReviewInfoResponse {

    private Long reviewId;
    private Long appointmentId;
    private ReviewRateType reviewRate;
    private String reviewComment;
    private LocalDateTime reviewCreateDate;

    @Builder
    public ReviewInfoResponse(Long reviewId, Long appointmentId, ReviewRateType reviewRate, String reviewComment, LocalDateTime reviewCreateDate) {
        this.reviewId = reviewId;
        this.appointmentId = appointmentId;
        this.reviewRate = reviewRate;
        this.reviewComment = reviewComment;
        this.reviewCreateDate = reviewCreateDate;
    }

    public static ReviewInfoResponse of(Review review) {
        return ReviewInfoResponse.builder()
                .reviewId(review.getReviewId())
                .appointmentId(review.getAppointmentId())
                .reviewRate(review.getReviewRate())
                .reviewComment(review.getReviewComment())
                .reviewCreateDate(review.getReviewCreateDate())
                .build();
    }
}
