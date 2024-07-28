package com.swyp3.babpool.domain.appointment.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AppointmentRejectMessage {
    private Long appointmentReceiverUserId;
    private String messageType;

    @Builder
    public AppointmentRejectMessage(Long appointmentReceiverUserId, AppointmentSocketMessageType messageType) {
        this.appointmentReceiverUserId = appointmentReceiverUserId;
        this.messageType = messageType.toString();
    }
}
