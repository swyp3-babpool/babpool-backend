package com.swyp3.babpool.domain.appointment.application.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
public class AppointmentHistoryRefuseResponse {

    private Long appointmentId;
    private Long appointmentReceiverProfileId;
    private String appointmentReceiverUserNickname;
    private String appointmentReceiverProfileImageUrl;
    private String appointmentStatus;
    private LocalDateTime refuseCreateDate;
    private String refuseType;

    @Builder
    public AppointmentHistoryRefuseResponse(Long appointmentId, Long appointmentReceiverProfileId, String appointmentReceiverUserNickname, String appointmentReceiverProfileImageUrl, String appointmentStatus, LocalDateTime refuseCreateDate, String refuseType) {
        this.appointmentId = appointmentId;
        this.appointmentReceiverProfileId = appointmentReceiverProfileId;
        this.appointmentReceiverUserNickname = appointmentReceiverUserNickname;
        this.appointmentReceiverProfileImageUrl = appointmentReceiverProfileImageUrl;
        this.appointmentStatus = appointmentStatus;
        this.refuseCreateDate = refuseCreateDate;
        this.refuseType = refuseType;
    }
}
