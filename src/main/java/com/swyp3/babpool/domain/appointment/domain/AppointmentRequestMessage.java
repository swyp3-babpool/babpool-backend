package com.swyp3.babpool.domain.appointment.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class AppointmentRequestMessage {

    private Long appointmentSenderUserId;
    private String messageType;

    @Builder
    public AppointmentRequestMessage(Long appointmentSenderUserId, AppointmentSocketMessageType messageType) {
        this.appointmentSenderUserId = appointmentSenderUserId;
        this.messageType = messageType.toString();
    }
}
