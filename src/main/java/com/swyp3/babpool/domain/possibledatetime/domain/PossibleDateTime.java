package com.swyp3.babpool.domain.possibledatetime.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
public class PossibleDateTime {

    private Long possibleDateTimeId;
    private LocalDateTime possibleDateTime;
    private PossibleDateTimeStatusType possibleDateTimeStatus;
    private Long userId;

    @Builder
    public PossibleDateTime(Long possibleDateTimeId, LocalDateTime possibleDateTime, PossibleDateTimeStatusType possibleDateTimeStatus, Long userId) {
        this.possibleDateTimeId = possibleDateTimeId;
        this.possibleDateTime = possibleDateTime;
        this.possibleDateTimeStatus = possibleDateTimeStatus;
        this.userId = userId;
    }
}
