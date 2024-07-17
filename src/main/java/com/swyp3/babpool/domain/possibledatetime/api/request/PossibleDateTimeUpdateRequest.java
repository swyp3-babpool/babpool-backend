package com.swyp3.babpool.domain.possibledatetime.api.request;

import jakarta.validation.constraints.Null;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@ToString
@Getter
public class PossibleDateTimeUpdateRequest {

    private List<LocalDateTime> possibleDateTimeAddList;
    private List<LocalDateTime> possibleDateTimeDelList;

    @Builder
    public PossibleDateTimeUpdateRequest(List<LocalDateTime> possibleDateTimeAddList, List<LocalDateTime> possibleDateTimeDelList) {
        this.possibleDateTimeAddList = possibleDateTimeAddList;
        this.possibleDateTimeDelList = possibleDateTimeDelList;
    }

}
