package com.swyp3.babpool.domain.possibledatetime.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class PossibleDateTime {

    private Long possibleDateTimeId;
    private String possibleDateTime;
    private String possibleDateTimeStatus;
    private Long userId;

    @Builder
    public PossibleDateTime(Long possibleDateTimeId, String possibleDateTime, String possibleDateTimeStatus, Long userId) {
        this.possibleDateTimeId = possibleDateTimeId;
        this.possibleDateTime = possibleDateTime;
        this.possibleDateTimeStatus = possibleDateTimeStatus;
        this.userId = userId;
    }
}
