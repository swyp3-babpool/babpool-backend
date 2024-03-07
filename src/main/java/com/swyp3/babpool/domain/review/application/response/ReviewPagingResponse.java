package com.swyp3.babpool.domain.review.application.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
public class ReviewPagingResponse {

    private Long reviewId;
    private Long appointmentId;
    private String reviewRate;
    private String reviewComment;
    private LocalDateTime reviewCreateDate;

    @Builder
    public ReviewPagingResponse(Long reviewId, Long appointmentId, String reviewRate, String reviewComment, LocalDateTime reviewCreateDate) {
        this.reviewId = reviewId;
        this.appointmentId = appointmentId;
        this.reviewRate = reviewRate;
        this.reviewComment = reviewComment;
        this.reviewCreateDate = reviewCreateDate;
    }
}
