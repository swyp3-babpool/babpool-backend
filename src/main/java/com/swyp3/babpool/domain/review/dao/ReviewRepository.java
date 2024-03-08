package com.swyp3.babpool.domain.review.dao;

import com.swyp3.babpool.domain.review.api.request.ReviewCreateRequest;
import com.swyp3.babpool.domain.review.api.request.ReviewUpdateRequest;
import com.swyp3.babpool.domain.review.application.response.ReviewCountByTypeResponse;
import com.swyp3.babpool.domain.review.application.response.ReviewPagingResponse;
import com.swyp3.babpool.domain.review.application.response.ReviewSaveResponse;
import com.swyp3.babpool.domain.review.domain.Review;
import com.swyp3.babpool.global.common.request.PagingRequestList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ReviewRepository {
    Optional<ReviewCountByTypeResponse> countReviewByType(Long profileId);

    Optional<Review> findByAppointmentId(@Param("appointmentId") Long targetAppointmentId);

    Optional<Review> findByReviewId(Long reviewId);

    int saveReview(ReviewCreateRequest reviewCreateRequest);

    int updateReview(ReviewUpdateRequest reviewUpdateRequest);

    boolean isReviewCreateAvailableTime(Long targetAppointmentId);

    Optional<Boolean> isReviewUpdateAvailableTime(Long targetAppointmentId);

    List<ReviewPagingResponse> findAllByPageable(PagingRequestList<?> pagingRequest);

    int countByPageable(Long profileId);

    List<ReviewPagingResponse> findAllByProfileIdAndLimit(@Param("profileId") Long profileId, @Param("limit") Integer limit);

}
