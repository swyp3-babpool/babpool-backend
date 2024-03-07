package com.swyp3.babpool.domain.review.dao;

import com.swyp3.babpool.domain.review.api.request.ReviewCreateRequest;
import com.swyp3.babpool.domain.review.api.request.ReviewUpdateRequest;
import com.swyp3.babpool.domain.review.application.response.ReviewCountByTypeResponse;
import com.swyp3.babpool.domain.review.application.response.ReviewPagingResponse;
import com.swyp3.babpool.domain.review.application.response.ReviewSaveResponse;
import com.swyp3.babpool.domain.review.domain.Review;
import com.swyp3.babpool.global.common.request.PagingRequestList;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ReviewRepository {
    Optional<ReviewCountByTypeResponse> countReviewByType(Long profileId);

    Optional<Object> findByAppointmentId(Long targetAppointmentId);

    Optional<Review> findByReviewId(Long reviewId);

    Optional<ReviewSaveResponse> saveReview(ReviewCreateRequest reviewCreateRequest);

    Optional<ReviewSaveResponse> updateReview(ReviewUpdateRequest reviewUpdateRequest);

    boolean isReviewCreateAvailableTime(Long targetAppointmentId);

    boolean isReviewUpdateAvailableTime(Long targetAppointmentId);

    List<ReviewPagingResponse> findAllByPageable(PagingRequestList<?> pagingRequest);

    int countByPageable(Long profileId);
}
