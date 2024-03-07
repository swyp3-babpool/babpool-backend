package com.swyp3.babpool.domain.review.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
public class Review {

    private Long reviewId;
    private Long appointmentId;
    private String reviewRate;
    private String reviewComment;
    private Boolean reviewDeleteFlag;
    private LocalDateTime reviewCreateDate;
    private LocalDateTime reviewModifyDate;

    @Builder
    public Review(Long reviewId, Long appointmentId, String reviewRate, String reviewComment, Boolean reviewDeleteFlag, LocalDateTime reviewCreateDate, LocalDateTime reviewModifyDate) {
        this.reviewId = reviewId;
        this.appointmentId = appointmentId;
        this.reviewRate = reviewRate;
        this.reviewComment = reviewComment;
        this.reviewDeleteFlag = reviewDeleteFlag;
        this.reviewCreateDate = reviewCreateDate;
        this.reviewModifyDate = reviewModifyDate;
    }
}
