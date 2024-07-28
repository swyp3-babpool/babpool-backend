package com.swyp3.babpool.domain.appointment.domain;

import lombok.Getter;

@Getter
public enum AppointmentStatus {

    WAITING,
    ACCEPTED,
    REJECTED,
    DONE,
    EXPIRED

}
