package com.swyp3.babpool.domain.review.dao;

import com.swyp3.babpool.domain.review.api.request.ReviewCreateRequest;
import com.swyp3.babpool.domain.review.api.request.ReviewUpdateRequest;
import com.swyp3.babpool.domain.review.application.response.ReviewCountByTypeResponse;
import com.swyp3.babpool.domain.review.application.response.ReviewPagingResponse;
import com.swyp3.babpool.domain.review.domain.Review;
import com.swyp3.babpool.global.common.request.PagingRequestList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ReviewRepository {

    // 테스트 코드 작성 완료
    Optional<ReviewCountByTypeResponse> countByTypeAndProfileId(Long profileId);

    // 테스트 코드 작성 완료
    Optional<Review> findByAppointmentId(@Param("appointmentId") Long appointmentId);

    // 테스트 코드 작성 완료
    Optional<Review> findByReviewId(Long reviewId);

    // 테스트 코드 작성 완료
    boolean isReviewCreateAvailableTime(Long targetAppointmentId);

    // 테스트 코드 작성 완료
    Optional<Boolean> isReviewUpdateAvailableTime(Long targetAppointmentId);

    List<ReviewPagingResponse> findAllByPageable(PagingRequestList<?> pagingRequest);

    int countByPageable(Long profileId);

    // 테스트 코드 작성 완료
    List<ReviewPagingResponse> findAllByProfileIdWithLimit(@Param("profileId") Long profileId, @Param("limit") Integer limit);

    // 테스트 코드 작성 완료
    int saveReview(ReviewCreateRequest reviewCreateRequest);

    // 테스트 코드 작성 완료
    int save(Review review);

    // 테스트 코드 작성 완료
    int updateReview(ReviewUpdateRequest reviewUpdateRequest);

}
