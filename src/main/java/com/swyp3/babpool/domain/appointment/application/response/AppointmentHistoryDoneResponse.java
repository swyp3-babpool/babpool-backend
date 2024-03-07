package com.swyp3.babpool.domain.appointment.application.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
public class AppointmentHistoryDoneResponse {

    private Long appointmentId;
    private Long appointmentReceiverProfileId;
    private String appointmentReceiverUserNickname;
    private String appointmentReceiverProfileImageUrl;
    private String appointmentStatus;
    private LocalDateTime appointment_fix_date_time;
    private String reviewRequired;

    @Builder
    public AppointmentHistoryDoneResponse(Long appointmentId, Long appointmentReceiverProfileId, String appointmentReceiverUserNickname, String appointmentReceiverProfileImageUrl, String appointmentStatus, LocalDateTime appointment_fix_date_time, String reviewRequired) {
        this.appointmentId = appointmentId;
        this.appointmentReceiverProfileId = appointmentReceiverProfileId;
        this.appointmentReceiverUserNickname = appointmentReceiverUserNickname;
        this.appointmentReceiverProfileImageUrl = appointmentReceiverProfileImageUrl;
        this.appointmentStatus = appointmentStatus;
        this.appointment_fix_date_time = appointment_fix_date_time;
        this.reviewRequired = reviewRequired;
    }

}
