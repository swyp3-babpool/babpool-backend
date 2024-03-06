package com.swyp3.babpool.domain.appointment.application.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
public class AppointmentSendResponse {

    private Long appointmentId;
    private Long appointmentReceiverProfileId;
    private String appointmentReceiverUserNickname;
    private String appointmentReceiverProfileImageUrl;
    private String appointmentFixDateTime;
    private String appointmentStatus;
    private LocalDateTime appointmentCreateDate;

    @Builder
    public AppointmentSendResponse(Long appointmentId, Long appointmentReceiverProfileId, String appointmentReceiverUserNickname, String appointmentReceiverProfileImageUrl, String appointmentFixDateTime, String appointmentStatus, LocalDateTime appointmentCreateDate) {
        this.appointmentId = appointmentId;
        this.appointmentReceiverProfileId = appointmentReceiverProfileId;
        this.appointmentReceiverUserNickname = appointmentReceiverUserNickname;
        this.appointmentReceiverProfileImageUrl = appointmentReceiverProfileImageUrl;
        this.appointmentFixDateTime = appointmentFixDateTime;
        this.appointmentStatus = appointmentStatus;
        this.appointmentCreateDate = appointmentCreateDate;
    }
}
