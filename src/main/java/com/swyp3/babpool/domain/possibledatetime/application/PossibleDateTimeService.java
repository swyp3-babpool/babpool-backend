package com.swyp3.babpool.domain.possibledatetime.application;

import com.swyp3.babpool.domain.possibledatetime.api.request.PossibleDateTimeUpdateRequest;
import com.swyp3.babpool.domain.possibledatetime.application.response.PossibleDateTimeResponse;
import com.swyp3.babpool.domain.possibledatetime.domain.PossibleDateTime;
import com.swyp3.babpool.domain.possibledatetime.domain.PossibleDateTimeStatusType;

import java.time.LocalDateTime;
import java.util.List;

public interface PossibleDateTimeService {
    PossibleDateTime throwExceptionIfAppointmentAlreadyAcceptedAtSameTime(Long targetProfileId, Long possibleDateTimeId, LocalDateTime possibleDateTime);

    boolean changeStatusAsReserved(Long possibleDateTimeId);

    List<PossibleDateTimeResponse> updatePossibleDateTime(Long userId, PossibleDateTimeUpdateRequest possibleDateTimeUpdateRequest);

    List<PossibleDateTimeResponse> getPossibleDateTimeList(Long userId);

    LocalDateTime getPossibleDateTimeByDateTimeId(Long possibleDateTimeId);

    Long findPossibleDateTimeIdByReceiverAndDateTimeAndStatus(Long receiverUserId, LocalDateTime possibleDateTime, PossibleDateTimeStatusType possibleDateTimeStatus);
}
