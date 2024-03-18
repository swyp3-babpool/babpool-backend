package com.swyp3.babpool.domain.appointment.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class AppointmentRequestMessage {

    private Long requesterProfileId;
    private String messageType;

    @Builder
    public AppointmentRequestMessage(Long requesterProfileId, AppointmentSocketMessageType messageType) {
        this.requesterProfileId = requesterProfileId;
        this.messageType = messageType.toString();
    }
}
