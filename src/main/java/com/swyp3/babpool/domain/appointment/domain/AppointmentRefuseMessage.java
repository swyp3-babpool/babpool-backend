package com.swyp3.babpool.domain.appointment.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AppointmentRefuseMessage {
    private Long requestProfileId;
    private String refuseMessage;

    @Builder
    public AppointmentRefuseMessage(Long requestProfileId, String refuseMessage) {
        this.requestProfileId = requestProfileId;
        this.refuseMessage = refuseMessage;
    }
}
