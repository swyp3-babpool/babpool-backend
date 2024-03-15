package com.swyp3.babpool.domain.possibledatetime.dao;

import com.swyp3.babpool.domain.profile.domain.PossibleDateAndTime;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PossibleDateTimeRepository {
    List<PossibleDateAndTime> findAllPossibleDateAndTimeByProfileIdAndNowDateWithoutAcceptOrDone(Long profileId);

    void deletePossibleDate(Long profileId, String date);

    void deletePossibleTime(Long profileId, List<Integer> timeList);

    void insertPossibleDate(Long profileId, String date);

    void insertPossibleTime(Long profileId, List<Integer> timeList);
}
