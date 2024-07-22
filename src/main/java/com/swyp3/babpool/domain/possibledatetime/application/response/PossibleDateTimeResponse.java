package com.swyp3.babpool.domain.possibledatetime.application.response;

import com.swyp3.babpool.domain.possibledatetime.domain.PossibleDateTime;
import com.swyp3.babpool.domain.possibledatetime.domain.PossibleDateTimeStatusType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@ToString
@Getter
public class PossibleDateTimeResponse {

    private Long possibleDateTimeId;
    private LocalDateTime possibleDateTime;
    private PossibleDateTimeStatusType possibleDateTimeStatus;

    @Builder
    public PossibleDateTimeResponse(Long possibleDateTimeId, LocalDateTime possibleDateTime, PossibleDateTimeStatusType possibleDateTimeStatus) {
        this.possibleDateTimeId = possibleDateTimeId;
        this.possibleDateTime = possibleDateTime;
        this.possibleDateTimeStatus = possibleDateTimeStatus;
    }

    public static PossibleDateTimeResponse from(PossibleDateTime possibleDateTime) {
        return PossibleDateTimeResponse.builder()
                    .possibleDateTimeId(possibleDateTime.getPossibleDateTimeId())
                    .possibleDateTime(possibleDateTime.getPossibleDateTime())
                    .possibleDateTimeStatus(possibleDateTime.getPossibleDateTimeStatus())
                    .build();
    }
}
