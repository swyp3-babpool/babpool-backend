package com.swyp3.babpool.domain.possibledatetime.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Null;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@ToString
@Getter
public class PossibleDateTimeUpdateRequest {

    @Schema(description = "추가할 가능한 일정 리스트", example = "[\"2024-08-01T12:00:00\", \"2024-08-02T12:00:00\"]")
    private List<LocalDateTime> possibleDateTimeAddList;
    @Schema(description = "삭제할 가능한 일정 리스트", example = "[]")
    private List<LocalDateTime> possibleDateTimeDelList;

    @Builder
    public PossibleDateTimeUpdateRequest(List<LocalDateTime> possibleDateTimeAddList, List<LocalDateTime> possibleDateTimeDelList) {
        this.possibleDateTimeAddList = possibleDateTimeAddList;
        this.possibleDateTimeDelList = possibleDateTimeDelList;
    }

}
