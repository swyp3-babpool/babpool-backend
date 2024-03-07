package com.swyp3.babpool.domain.review.api;

import com.swyp3.babpool.domain.review.application.response.ReviewPagingResponse;
import com.swyp3.babpool.domain.review.application.response.ReviewSaveResponse;
import com.swyp3.babpool.domain.review.api.request.ReviewUpdateRequest;
import com.swyp3.babpool.domain.review.api.request.ReviewCreateRequest;
import com.swyp3.babpool.domain.review.application.ReviewService;
import com.swyp3.babpool.domain.review.application.response.ReviewCountByTypeResponse;
import com.swyp3.babpool.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ReviewApi {

    private final ReviewService reviewService;

    /**
     * 특정 프로필이 받은 모든 리뷰 조회
     */
    @GetMapping("/api/review/list")
    public ApiResponse<Page<ReviewPagingResponse>> getReviewListPaging(
            @RequestParam("profileId") Long profileId,
            @PageableDefault(size = 10)
            @SortDefault(sort = "review_create_date", direction = Sort.Direction.DESC) Pageable pageable) {
        return ApiResponse.ok(reviewService.getReviewList(profileId, pageable));
    }


    /**
     * 특정 프로필의 리뷰 타입별 개수 조회
     * @param profileId
     * @return
     */
    @GetMapping("/api/review/{profileId}/count")
    public ApiResponse<ReviewCountByTypeResponse> getReviewCountByType(@PathVariable("profileId") Long profileId) {
        return ApiResponse.ok(reviewService.getReviewCountByType(profileId));
    }

    /**
     * 신규 리뷰 작성
     * @param request
     * @return
     */
    @PostMapping("/api/review/create")
    public ApiResponse<ReviewSaveResponse> createReview(@RequestAttribute(value = "userId") Long userId,
                                                          @RequestBody ReviewCreateRequest request) {
        return ApiResponse.ok(reviewService.createReview(request.setReviewerUserId(userId)));
    }

    /**
     * 리뷰 상세 조회
     */
    @GetMapping("/api/review/{appointmentId}")
    public ApiResponse<ReviewSaveResponse> getReview(@PathVariable("appointmentId") Long appointmentId) {
        return ApiResponse.ok(reviewService.getReviewInfo(appointmentId));
    }

    /**
     * 리뷰 수정
     * @param request
     * @return
     */
    @PutMapping("/api/review/update")
    public ApiResponse<ReviewSaveResponse> updateReview(@RequestAttribute(value = "userId") Long userId,
                                                          @RequestBody ReviewUpdateRequest request) {
        return ApiResponse.ok(reviewService.updateReview(request.setReviewerUserId(userId)));
    }

}
