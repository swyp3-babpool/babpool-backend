package com.swyp3.babpool.domain.appointment.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AppointmentAcceptMessage {
    private Long requestProfileId;
    private String acceptMessage;

    @Builder
    public AppointmentAcceptMessage(Long requestProfileId, String acceptMessage) {
        this.requestProfileId = requestProfileId;
        this.acceptMessage = acceptMessage;
    }
}
