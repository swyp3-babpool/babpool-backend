package com.swyp3.babpool.domain.appointment.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AppointmentRejectMessage {
    private Long requestProfileId;
    private String rejectMessage;

    @Builder
    public AppointmentRejectMessage(Long requestProfileId, String rejectMessage) {
        this.requestProfileId = requestProfileId;
        this.rejectMessage = rejectMessage;
    }
}
