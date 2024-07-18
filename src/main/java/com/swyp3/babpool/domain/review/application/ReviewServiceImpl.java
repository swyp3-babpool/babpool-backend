package com.swyp3.babpool.domain.review.application;

import com.swyp3.babpool.domain.appointment.dao.AppointmentRepository;
import com.swyp3.babpool.domain.appointment.domain.Appointment;
import com.swyp3.babpool.domain.review.api.request.ReviewCreateRequest;
import com.swyp3.babpool.domain.review.api.request.ReviewUpdateRequest;
import com.swyp3.babpool.domain.review.application.response.ReviewCountByTypeResponse;
import com.swyp3.babpool.domain.review.application.response.ReviewInfoResponse;
import com.swyp3.babpool.domain.review.application.response.ReviewPagingResponse;
import com.swyp3.babpool.domain.review.application.response.ReviewSaveResponse;
import com.swyp3.babpool.domain.review.dao.ReviewRepository;
import com.swyp3.babpool.domain.review.exception.ReviewErrorCode;
import com.swyp3.babpool.domain.review.exception.ReviewException;
import com.swyp3.babpool.global.common.request.PagingRequestList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    public ReviewCountByTypeResponse getReviewCountByType(Long profileId) {
        return reviewRepository.countByTypeAndProfileId(profileId)
                .orElseThrow(() -> new ReviewException(ReviewErrorCode.NOT_FOUND_REVIEW,"요청된 프로필에 리뷰가 존재하지 않습니다."));
    }

    @Override
    public ReviewSaveResponse createReview(ReviewCreateRequest reviewCreateRequest) {
        validateIsSameAppointmentRequest(reviewCreateRequest.getAppointmentId(), reviewCreateRequest.getReviewerUserId());

        reviewRepository.findByAppointmentId(reviewCreateRequest.getAppointmentId()).ifPresent(review -> {
            throw new ReviewException(ReviewErrorCode.ALREADY_EXIST_REVIEW,"잘못된 요청. 이미 리뷰가 존재합니다.");
        });

        // 리뷰 작성 가능 시간 체크
        if(!reviewRepository.isReviewCreateAvailableTime(reviewCreateRequest.getAppointmentId())){
            throw new ReviewException(ReviewErrorCode.REVIEW_CREATE_REQUEST_FAIL,"리뷰 작성 가능 시간이 아닙니다.");
        }

        // 리뷰 생성
        int resultRows = reviewRepository.saveReview(reviewCreateRequest);
        if(resultRows != 1){
            throw new ReviewException(ReviewErrorCode.REVIEW_CREATE_REQUEST_FAIL,"리뷰 작성에 실패하였습니다.");
        }

        // 리뷰 생성 후 appointment 테이블에 DONE 상태로 변경
        int updatedRows = appointmentRepository.updateAppointmentStatus(reviewCreateRequest.getAppointmentId(), "DONE");
        if(updatedRows != 1){
            throw new ReviewException(ReviewErrorCode.REVIEW_CREATE_REQUEST_FAIL,"리뷰 작성에 실패하였습니다. appointment 테이블 상태 변경 실패.");
        }

        return ReviewSaveResponse.of(reviewRepository.findByReviewId(reviewCreateRequest.getReviewId()).orElseThrow(
                () -> new ReviewException(ReviewErrorCode.NOT_FOUND_REVIEW,"리뷰 정보를 찾을 수 없습니다.")
        ));
    }

    @Override
    public ReviewSaveResponse updateReview(ReviewUpdateRequest reviewUpdateRequest) {
        validateIsSameAppointmentRequest(reviewUpdateRequest.getAppointmentId(), reviewUpdateRequest.getReviewerUserId());

        // 리뷰 수정 가능 시간 체크
        Optional<Boolean> isReviewUpdateAvailable = reviewRepository.isReviewUpdateAvailableTime(reviewUpdateRequest.getReviewId());
        isReviewUpdateAvailable.orElseThrow(
                () -> new ReviewException(ReviewErrorCode.NOT_FOUND_REVIEW, "수정할 리뷰를 찾을 수 없습니다.")
        );
        if(!isReviewUpdateAvailable.get()){
            throw new ReviewException(ReviewErrorCode.REVIEW_UPDATE_REQUEST_FAIL,"리뷰 수정 가능 시간이 아닙니다.");
        }

        int updatedRows = reviewRepository.updateReview(reviewUpdateRequest);
        if(updatedRows != 1){
            throw new ReviewException(ReviewErrorCode.REVIEW_UPDATE_REQUEST_FAIL,"리뷰 수정에 실패하였습니다.");
        }

        return ReviewSaveResponse.of(reviewRepository.findByReviewId(reviewUpdateRequest.getReviewId()).orElseThrow(
                () -> new ReviewException(ReviewErrorCode.NOT_FOUND_REVIEW,"리뷰 정보를 찾을 수 없습니다.")
        ));
    }

    /**
     * request의 targetAppointmentId 으로 조회한 약속의 appointment_request_id 와 일치하는지 확인
     * @param targetAppointmentId
     * @param reviewerUserId
     */
    private void validateIsSameAppointmentRequest(Long targetAppointmentId, Long reviewerUserId) {
        Appointment targetAppointment = appointmentRepository.findByAppointmentId(targetAppointmentId);
        if(targetAppointment.getAppointmentSenderId() != reviewerUserId){
            throw new ReviewException(ReviewErrorCode.NOT_VALID_REVIEW_REQUEST,"appointment_request_id 와 리뷰 요청자가 일치하지 않습니다.");
        }
    }

    @Override
    public ReviewInfoResponse getReviewInfo(Long appointmentId) {
        return reviewRepository.findByAppointmentId(appointmentId)
                .map(ReviewInfoResponse::of)
                .orElseThrow(() -> new ReviewException(ReviewErrorCode.NOT_FOUND_REVIEW,"리뷰 정보를 찾을 수 없습니다."));
    }

    @Override
    public Page<ReviewPagingResponse> getReviewList(Long profileId, Pageable pageable) {
        PagingRequestList<?> pagingRequest = PagingRequestList.builder()
                .condition(profileId)
                .pageable(pageable)
                .build();
        List<ReviewPagingResponse> reviewPagingResponse = null;
        int counts = 0;
        try {
            reviewPagingResponse = reviewRepository.findAllByPageable(pagingRequest);
            log.info("리뷰 리스트 조회 결과. reviewPagingResponse: {}", reviewPagingResponse);
            counts = reviewRepository.countByPageable(profileId);
        } catch (Exception e) {
            log.error("리뷰 리스트 조회 중 오류 발생. {}", e.getMessage());
            log.error("{}", e.getStackTrace());
            throw new ReviewException(ReviewErrorCode.REVIEW_LIST_ERROR, "리뷰 리스트 조회 중 오류가 발생했습니다.");
        }
        return new PageImpl<>(reviewPagingResponse, pageable, counts);
    }


    @Override
    public List<ReviewPagingResponse> getReviewListForProfileDetail(Long profileId, Integer limit) {
        return reviewRepository.findAllByProfileIdWithLimit(profileId, limit);
    }

}
