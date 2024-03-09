package com.swyp3.babpool.domain.appointment.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AppointmentRejectMessage {
    private Long requestProfileId;
    private String refuseMessage;

    @Builder
    public AppointmentRejectMessage(Long requestProfileId, String refuseMessage) {
        this.requestProfileId = requestProfileId;
        this.refuseMessage = refuseMessage;
    }
}
