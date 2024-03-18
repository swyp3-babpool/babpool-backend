package com.swyp3.babpool.domain.appointment.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AppointmentAcceptMessage {
    private Long receiverProfileId;
    private String messageType;

    @Builder
    public AppointmentAcceptMessage(Long receiverProfileId, AppointmentSocketMessageType messageType) {
        this.receiverProfileId = receiverProfileId;
        this.messageType = messageType.toString();
    }
}
