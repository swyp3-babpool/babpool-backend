package com.swyp3.babpool.domain.facade;

import com.swyp3.babpool.domain.appointment.application.AppointmentService;
import com.swyp3.babpool.domain.appointment.domain.AppointmentStatus;
import com.swyp3.babpool.domain.review.api.request.ReviewCreateRequest;
import com.swyp3.babpool.domain.review.api.request.ReviewUpdateRequest;
import com.swyp3.babpool.domain.review.application.ReviewService;
import com.swyp3.babpool.domain.review.application.response.ReviewSaveResponse;
import com.swyp3.babpool.domain.review.exception.ReviewErrorCode;
import com.swyp3.babpool.domain.review.exception.ReviewException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AppointmentReviewFacade {

    private final AppointmentService appointmentService;
    private final ReviewService reviewService;

    @Transactional
    public ReviewSaveResponse createReview(ReviewCreateRequest reviewCreateRequest) {
        validateIsSameAppointmentRequest(reviewCreateRequest.getAppointmentId(), reviewCreateRequest.getReviewerUserId());

        ReviewSaveResponse createdReview = reviewService.createReview(reviewCreateRequest);
        // 리뷰 생성 후 appointment 테이블에 DONE 상태로 변경
        int updatedRows = appointmentService.updateAppointmentStatusTo(AppointmentStatus.DONE, reviewCreateRequest.getAppointmentId());
        if(updatedRows != 1){
            throw new ReviewException(ReviewErrorCode.REVIEW_CREATE_REQUEST_FAIL,"리뷰 작성에 실패하였습니다. appointment 테이블 상태 변경 실패.");
        }
        return createdReview;
    }

    public ReviewSaveResponse updateReview(ReviewUpdateRequest reviewUpdateRequest) {
        validateIsSameAppointmentRequest(reviewUpdateRequest.getAppointmentId(), reviewUpdateRequest.getReviewerUserId());
        return reviewService.updateReview(reviewUpdateRequest);
    }

    /**
     * 리뷰 생성 또는 수정 API 를 호출한 사용자의 Appointment 가 맞는지 검증하기 위해,
     * targetAppointmentId 로 조회한 Appointment 의 appointmentSenderId 와 reviewerUserId 가 일치하는지 검증
     * @param targetAppointmentId
     * @param reviewerUserId
     */
    private void validateIsSameAppointmentRequest(Long targetAppointmentId, Long reviewerUserId) {
        Long targetAppointmentSenderId = appointmentService.getAppointmentSenderId(targetAppointmentId);
        if(!targetAppointmentSenderId.equals(reviewerUserId)){
            throw new ReviewException(ReviewErrorCode.NOT_VALID_REVIEW_REQUEST,"appointment_request_id 와 리뷰 요청자가 일치하지 않습니다.");
        }
    }
}
