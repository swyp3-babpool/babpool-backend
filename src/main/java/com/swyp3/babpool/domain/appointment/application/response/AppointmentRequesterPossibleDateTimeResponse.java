package com.swyp3.babpool.domain.appointment.application.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class AppointmentRequesterPossibleDateTimeResponse {
    private Long possibleTimeId;
    private Long possibleDateId;
    private LocalDate possibleDate;
    private Integer possibleTimeStart;
}
