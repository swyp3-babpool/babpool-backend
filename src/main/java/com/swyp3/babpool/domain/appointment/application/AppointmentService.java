package com.swyp3.babpool.domain.appointment.application;

import com.swyp3.babpool.domain.appointment.api.request.AppointmentAcceptRequest;
import com.swyp3.babpool.domain.appointment.api.request.AppointmentCreateRequest;
import com.swyp3.babpool.domain.appointment.api.request.AppointmentCreateRequestV1;
import com.swyp3.babpool.domain.appointment.api.request.AppointmentRejectRequest;
import com.swyp3.babpool.domain.appointment.application.response.*;
import com.swyp3.babpool.domain.appointment.application.response.appointmentdetail.AppointmentDetailResponse;

import java.util.List;

public interface AppointmentService {
    AppointmentCreateResponse makeAppointment(AppointmentCreateRequest appointmentCreateRequest);

    List<AppointmentSendResponse> getSendAppointmentList(Long userId);

    List<AppointmentReceiveResponse> getReceiveAppointmentList(Long userId);

    List<AppointmentHistoryDoneResponse> getDoneAppointmentList(Long userId);

    List<AppointmentHistoryRefuseResponse> getRefusedAppointmentList(Long userId);

    List<AppointmentPossibleDateTimeResponse> getAppointmentPossibleDateTime(Long profileId);

    AppointmentRejectResponse rejectAppointment(AppointmentRejectRequest appointmentRejectRequest, Long userId);

    AppointmentAcceptResponse acceptAppointment(AppointmentAcceptRequest appointmentAcceptRequest, Long userId);

    AppointmentDetailResponse getAppointmentDetail(Long userId, Long appointmentId);

    AppointmentCancelResponse cancelAppointmentRequested(Long userId, Long appointmentId);

    AppointmentRefuseDetailResponse getRefuseAppointmentDetail(Long userId, Long appointmentId);

    AppointmentCreateResponse makeAppointmentResolveConcurrency(AppointmentCreateRequestV1 appointmentCreateRequest);
}
