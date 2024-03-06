package com.swyp3.babpool.domain.appointment.application.response;

import com.swyp3.babpool.domain.appointment.domain.Appointment;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class AppointmentCreateResponse {

    private Long appointmentId;
    private Long targetProfileId;
    private String appointmentStatus;

    @Builder
    public AppointmentCreateResponse(Long appointmentId, Long targetProfileId, String appointmentStatus) {
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
