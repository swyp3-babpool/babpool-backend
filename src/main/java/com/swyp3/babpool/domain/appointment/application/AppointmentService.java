package com.swyp3.babpool.domain.appointment.application;

import com.swyp3.babpool.domain.appointment.api.request.AppointmentCreateRequest;
import com.swyp3.babpool.domain.appointment.application.response.AppointmentReceiveResponse;
import com.swyp3.babpool.domain.appointment.application.response.AppointmentCreateResponse;
import com.swyp3.babpool.domain.appointment.application.response.AppointmentSendResponse;

import java.util.List;

public interface AppointmentService {
    AppointmentCreateResponse makeAppointment(AppointmentCreateRequest appointmentCreateRequest);

    List<AppointmentSendResponse> getSendAppointmentList(Long userId);

    List<AppointmentReceiveResponse> getReceiveAppointmentList(Long userId);
}
