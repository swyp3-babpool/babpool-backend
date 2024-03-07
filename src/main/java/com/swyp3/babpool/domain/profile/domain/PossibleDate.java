package com.swyp3.babpool.domain.profile.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PossibleDate {
    private Long id;
    private Long profileId;
    private String possibleDate;

    @Builder
    public PossibleDate(Long profile_id, String possible_date) {
        this.profileId = profile_id;
        this.possibleDate = possible_date;
    }

}
