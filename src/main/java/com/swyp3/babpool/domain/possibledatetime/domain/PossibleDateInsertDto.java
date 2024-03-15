package com.swyp3.babpool.domain.possibledatetime.domain;

import lombok.Builder;
import lombok.Getter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
public class PossibleDateInsertDto {
    private Long possibleDateId;
    private Long profileId;
    private String date;


    @Builder
    public PossibleDateInsertDto(Long possibleDateId, Long profileId, String date) {
        this.possibleDateId = possibleDateId;
        this.profileId = profileId;
        this.date = date;
    }

    public void setPossibleDateId(Long possibleDateId) {
        this.possibleDateId = possibleDateId;
    }
}
