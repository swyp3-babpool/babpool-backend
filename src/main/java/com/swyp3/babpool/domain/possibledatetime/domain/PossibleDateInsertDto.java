package com.swyp3.babpool.domain.possibledatetime.domain;

import lombok.Getter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
public class PossibleDateInsertDto {
    private Long possibleDateId;
    private Long profileId;
    private Date date;

    public PossibleDateInsertDto(Long profileId, String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        this.date = sdf.parse(date);
        this.profileId=profileId;
    }
}
