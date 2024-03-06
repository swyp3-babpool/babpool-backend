package com.swyp3.babpool.domain.appointment.api;

import com.swyp3.babpool.domain.appointment.application.AppointmentService;
import com.swyp3.babpool.domain.appointment.api.request.AppointmentCreateRequest;
import com.swyp3.babpool.domain.appointment.application.response.AppointmentReceiveResponse;
import com.swyp3.babpool.domain.appointment.application.response.AppointmentCreateResponse;
import com.swyp3.babpool.domain.appointment.application.response.AppointmentSendResponse;
import com.swyp3.babpool.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AppointmentApi {

    private final AppointmentService appointmentService;

    /**
     * 밥약 요청 API
     */
    @PostMapping("/api/appointment")
    public ApiResponse<AppointmentCreateResponse> makeAppointment(@RequestAttribute(value = "userId", required = false) Long userId,
                                                                  @RequestBody AppointmentCreateRequest appointmentCreateRequest) {
        appointmentCreateRequest.setRequesterUserId(userId);
        return ApiResponse.ok(appointmentService.makeAppointment(appointmentCreateRequest));
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


}
