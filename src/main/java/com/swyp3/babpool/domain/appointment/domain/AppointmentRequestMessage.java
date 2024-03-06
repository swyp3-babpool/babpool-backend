package com.swyp3.babpool.domain.appointment.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class AppointmentRequestMessage {

    private Long targetProfileId;
    private String message;

    @Builder
    public AppointmentRequestMessage(Long targetProfileId, String message) {
        this.targetProfileId = targetProfileId;
        this.message = message;
    }
}
