package com.swyp3.babpool.domain.appointment.application.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class AppointmentCancelResponse {

    private final Long appointmentId;
    private final String appointmentCancelResult;

    @Builder
    public AppointmentCancelResponse(Long appointmentId, String appointmentCancelResult) {
        this.appointmentId = appointmentId;
        this.appointmentCancelResult = appointmentCancelResult;
    }
}
