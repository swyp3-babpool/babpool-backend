package com.swyp3.babpool.domain.appointment.application;

import com.swyp3.babpool.domain.appointment.api.request.AppointmentCreateRequest;
import com.swyp3.babpool.domain.appointment.api.request.AppointmentRefuseRequest;
import com.swyp3.babpool.domain.appointment.application.response.*;

import java.util.List;

public interface AppointmentService {
    AppointmentCreateResponse makeAppointment(AppointmentCreateRequest appointmentCreateRequest);

    List<AppointmentSendResponse> getSendAppointmentList(Long userId);

    List<AppointmentReceiveResponse> getReceiveAppointmentList(Long userId);

    List<AppointmentHistoryDoneResponse> getDoneAppointmentList(Long userId);

    List<AppointmentHistoryRefuseResponse> getRefuseAppointmentList(Long userId);

    List<AppointmentPossibleDateTimeResponse> getAppointmentPossibleDateTime(Long profileId);

    AppointmentRefuseResponse refuseAppointment(AppointmentRefuseRequest appointmentRefuseRequest);
}
