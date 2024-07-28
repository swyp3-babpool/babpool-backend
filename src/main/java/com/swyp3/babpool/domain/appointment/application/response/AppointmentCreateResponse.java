package com.swyp3.babpool.domain.appointment.application.response;

import com.swyp3.babpool.domain.appointment.domain.Appointment;
import com.swyp3.babpool.domain.appointment.domain.AppointmentStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class AppointmentCreateResponse {

    private Long appointmentId;
    private Long targetProfileId;
    private AppointmentStatus appointmentStatus;

    @Builder
    public AppointmentCreateResponse(Long appointmentId, Long targetProfileId, AppointmentStatus appointmentStatus) {
        this.appointmentId = appointmentId;
        this.targetProfileId = targetProfileId;
        this.appointmentStatus = appointmentStatus;
    }

    public static AppointmentCreateResponse of(Appointment appointment, Long targetProfileId) {
        return AppointmentCreateResponse.builder()
                .appointmentId(appointment.getAppointmentId())
                .targetProfileId(targetProfileId)
                .appointmentStatus(appointment.getAppointmentStatus())
                .build();
    }
}
