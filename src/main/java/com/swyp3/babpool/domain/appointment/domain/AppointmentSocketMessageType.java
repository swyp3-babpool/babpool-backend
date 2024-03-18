package com.swyp3.babpool.domain.appointment.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AppointmentSocketMessageType {

    APPOINTMENT_REQUESTED("APPOINTMENT_REQUESTED"),
    APPOINTMENT_ACCEPTED("APPOINTMENT_ACCEPTED"),
    APPOINTMENT_REJECTED("APPOINTMENT_REJECTED"),
    ;

    private final String messageType;
}
