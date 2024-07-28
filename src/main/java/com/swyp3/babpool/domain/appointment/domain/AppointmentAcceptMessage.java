package com.swyp3.babpool.domain.appointment.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AppointmentAcceptMessage {
    private Long appointmentReceiverUserId;
    private String messageType;

    @Builder
    public AppointmentAcceptMessage(Long appointmentReceiverUserId, AppointmentSocketMessageType messageType) {
        this.appointmentReceiverUserId = appointmentReceiverUserId;
        this.messageType = messageType.toString();
    }
}
