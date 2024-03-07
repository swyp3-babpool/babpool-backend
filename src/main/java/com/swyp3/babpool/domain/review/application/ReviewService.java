package com.swyp3.babpool.domain.review.application;

import com.swyp3.babpool.domain.review.api.request.ReviewCreateRequest;
import com.swyp3.babpool.domain.review.api.request.ReviewUpdateRequest;
import com.swyp3.babpool.domain.review.application.response.ReviewCountByTypeResponse;
import com.swyp3.babpool.domain.review.application.response.ReviewPagingResponse;
import com.swyp3.babpool.domain.review.application.response.ReviewSaveResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewService {
    ReviewCountByTypeResponse getReviewCountByType(Long profileId);

    ReviewSaveResponse createReview(ReviewCreateRequest request);

    ReviewSaveResponse updateReview(ReviewUpdateRequest reviewCreateRequest);

    ReviewSaveResponse getReviewInfo(Long appointmentId);

    Page<ReviewPagingResponse> getReviewList(Long profileId, Pageable pageable);
}
