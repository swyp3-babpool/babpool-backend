package com.swyp3.babpool.domain.possibledatetime.application;

import com.swyp3.babpool.domain.possibledatetime.domain.PossibleDateTime;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PossibleDateTimeService {
    PossibleDateTime throwExceptionIfAppointmentAlreadyAcceptedAtSameTime(Long targetProfileId, Long possibleDateTimeId, LocalDateTime possibleDateTime);

    void changeStatusAsReserved(Long possibleDateTimeId);
}
