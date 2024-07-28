package com.swyp3.babpool.domain.reject.application;

import com.swyp3.babpool.domain.appointment.api.request.AppointmentRejectRequest;
import com.swyp3.babpool.domain.appointment.application.response.AppointmentRefuseDetailResponse;

public interface RejectService {
    void createReject(AppointmentRejectRequest appointmentRejectRequest);

}
