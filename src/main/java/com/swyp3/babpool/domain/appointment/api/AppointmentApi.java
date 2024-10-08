package com.swyp3.babpool.domain.appointment.api;

import com.swyp3.babpool.domain.appointment.api.request.AppointmentAcceptRequest;
import com.swyp3.babpool.domain.appointment.api.request.AppointmentCreateRequest;
import com.swyp3.babpool.domain.appointment.api.request.AppointmentRejectRequest;
import com.swyp3.babpool.domain.appointment.application.AppointmentService;
import com.swyp3.babpool.domain.appointment.application.response.*;
import com.swyp3.babpool.domain.appointment.application.response.appointmentdetail.AppointmentDetailResponse;
import com.swyp3.babpool.domain.facade.ProfilePossibleDateTimeFacade;
import com.swyp3.babpool.global.common.response.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
public class AppointmentApi {

    private final AppointmentService appointmentService;
    private final ProfilePossibleDateTimeFacade profilePossibleDateTimeFacade;

    /**
     * 밥약 요청 API : 동시성 처리를 위한 버전
     */
    @PostMapping("/api/appointment")
    public ApiResponse<AppointmentCreateResponse> createAppointment(@RequestAttribute(value = "userId", required = false) Long userId,
                                                                    @RequestBody @Validated AppointmentCreateRequest appointmentCreateRequest) {
        appointmentCreateRequest.setSenderUserId(userId);
        appointmentCreateRequest.setReceiverUserId(
                profilePossibleDateTimeFacade.getUserIdByProfileId(appointmentCreateRequest.getTargetProfileId()));
        return ApiResponse.ok(appointmentService.makeAppointmentResolveConcurrency(appointmentCreateRequest));
    }

    /**
     * 요청한 밥약 리스트 조회 API
     */
    @GetMapping("/api/appointment/list/send")
    public ApiResponse<List<AppointmentSendResponse>> getSendAppointmentList(@RequestAttribute(value = "userId", required = false) Long userId) {
        return ApiResponse.ok(appointmentService.getSendAppointmentList(userId));
    }

    /**
     * 요청받은 밥약 리스트 조회 API
     */
    @GetMapping("/api/appointment/list/receive")
    public ApiResponse<List<AppointmentReceiveResponse>> getReceiveAppointmentList(@RequestAttribute(value = "userId", required = false) Long userId) {
        return ApiResponse.ok(appointmentService.getReceiveAppointmentList(userId));
    }

    /**
     * 밥약 히스토리 - 완료(DONE) 리스트 조회 API
     */
    @GetMapping("/api/appointment/list/done")
    public ApiResponse<List<AppointmentHistoryDoneResponse>> getDoneAppointmentList(@RequestAttribute(value = "userId", required = false) Long userId) {
        return ApiResponse.ok(appointmentService.getDoneAppointmentList(userId));
    }

    /**
     * 밥약 히스토리 - 거절(EXPIRE, REJECT)당한 리스트 조회 API
     */
    @GetMapping("/api/appointment/list/refuse")
    public ApiResponse<List<AppointmentHistoryRefuseResponse>> getRefusedAppointmentList(@RequestAttribute(value = "userId", required = false) Long userId) {
        return ApiResponse.ok(appointmentService.getRefusedAppointmentList(userId));
    }

    /**
     * 밥약 거절 사유 조회 API
     */
    @GetMapping("/api/appointment/refuse/{appointmentId}")
    public ApiResponse<AppointmentRefuseDetailResponse> getRefuseAppointmentDetail(@RequestAttribute(value="userId", required = false) Long userId,
                                                                                   @PathVariable("appointmentId") Long appointmentId){
        return ApiResponse.ok(appointmentService.getRefuseAppointmentDetail(userId, appointmentId));
    }

    /**
     * 밥약 요청 거절 API
     */
    @PostMapping("/api/appointment/reject")
    public ApiResponse<AppointmentRejectResponse> rejectAppointment(@RequestAttribute(value = "userId", required = false) Long userId,
                                                                    @RequestBody @Valid AppointmentRejectRequest appointmentRejectRequest) {
        return ApiResponse.ok(appointmentService.rejectAppointment(appointmentRejectRequest,userId));
    }

    /**
     * 밥약 요청 수락 API
     */
    @PostMapping("/api/appointment/accept")
    public ApiResponse<AppointmentAcceptResponse> acceptAppointment(@RequestAttribute(value = "userId", required = false) Long userId,
                                                                    @RequestBody @Valid AppointmentAcceptRequest appointmentAcceptRequest){
        return ApiResponse.ok(appointmentService.acceptAppointment(appointmentAcceptRequest,userId));
    }


    /**
     * 밥약 상세조회 API
     */
    @GetMapping("/api/appointment/detail/{appointmentId}")
    public ApiResponse<AppointmentDetailResponse> getAppointmentDetail(@RequestAttribute(value="userId", required = false) Long userId,
                                                                       @PathVariable("appointmentId") Long appointmentId){
        return ApiResponse.ok(appointmentService.getAppointmentDetail(userId,appointmentId));
    }

    /**
     * 밥약 요청 취소 API
     */
    @PostMapping("/api/appointment/cancel/{appointmentId}")
    public ApiResponse<AppointmentCancelResponse> cancelAppointment(@RequestAttribute(value="userId", required = false) Long userId,
                                                                   @PathVariable("appointmentId") Long appointmentId){
        return ApiResponse.ok(appointmentService.cancelAppointmentRequested(userId,appointmentId));
    }


}
