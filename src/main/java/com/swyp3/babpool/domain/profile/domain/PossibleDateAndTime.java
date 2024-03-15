package com.swyp3.babpool.domain.profile.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ToString
@Getter
public class PossibleDateAndTime {

    private Long possibleDateId;
    private String possibleDate;
    private List<Long> possibleTimeIdList;
    private List<Integer> possibleTimeList;

    @Builder
    public PossibleDateAndTime(Long possibleDateId, String possibleDate, String possibleTimeIdList, String possibleTimeList) {
        this.possibleDateId = possibleDateId;
        this.possibleDate = possibleDate;
        this.possibleTimeIdList = Arrays.stream(possibleTimeIdList.split(","))
                                        .map(Long::valueOf)
                                        .collect(Collectors.toList());
        this.possibleTimeList = Arrays.stream(possibleTimeList.split(","))
                                      .mapToInt(Integer::parseInt)
                                      .boxed().collect(Collectors.toList());
    }

    @Builder
    public PossibleDateAndTime(Long possibleDateId, String possibleDate, List<Long> possibleTimeIdList, List<Integer> possibleTimeList) {
        this.possibleDateId = possibleDateId;
        this.possibleDate = possibleDate;
        this.possibleTimeIdList = possibleTimeIdList;
        this.possibleTimeList = possibleTimeList;
    }

}
