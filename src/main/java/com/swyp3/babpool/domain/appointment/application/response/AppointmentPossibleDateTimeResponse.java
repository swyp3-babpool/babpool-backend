package com.swyp3.babpool.domain.appointment.application.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;

@ToString
@Getter
public class AppointmentPossibleDateTimeResponse {

    private Long profileId;
    private Long possibleTimeId;
    private Long possibleDateId;
    private LocalDate possibleDate;
    private Integer possibleTime;

    @Builder
    public AppointmentPossibleDateTimeResponse(Long profileId, Long possibleTimeId, Long possibleDateId, LocalDate possibleDate, Integer possibleTime) {
        this.profileId = profileId;
        this.possibleTimeId = possibleTimeId;
        this.possibleDateId = possibleDateId;
        this.possibleDate = possibleDate;
        this.possibleTime = possibleTime;
    }
}
