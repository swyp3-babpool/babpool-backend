package com.swyp3.babpool.domain.possibledatetime.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class PossibleDateTime {

    private Long possibleDateTimeId;
    private String possibleDateTimeDate;
    private String possibleDateTimeTime;
    private String possibleDateTimeRange;
    private String possibleDateTimeStatus;
    private Long profileId;

    @Builder
    public PossibleDateTime(Long possibleDateTimeId, String possibleDateTimeDate, String possibleDateTimeTime, String possibleDateTimeRange, String possibleDateTimeStatus, Long profileId) {
        this.possibleDateTimeId = possibleDateTimeId;
        this.possibleDateTimeDate = possibleDateTimeDate;
        this.possibleDateTimeTime = possibleDateTimeTime;
        this.possibleDateTimeRange = possibleDateTimeRange;
        this.possibleDateTimeStatus = possibleDateTimeStatus;
        this.profileId = profileId;
    }
}
