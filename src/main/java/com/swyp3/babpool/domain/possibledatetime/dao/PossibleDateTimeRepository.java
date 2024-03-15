package com.swyp3.babpool.domain.possibledatetime.dao;

import com.swyp3.babpool.domain.profile.domain.PossibleDateAndTime;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PossibleDateTimeRepository {
    List<PossibleDateAndTime> findAllPossibleDateAndTimeByProfileIdAndNowDateWithoutAcceptOrDone(Long profileId);

    void deletePossibleDate(Long profileId, Long dateId);

    void deletePossibleTime(Long profileId, Long timeId);

    void insertPossibleDate(Long profileId, String date);

    void insertPossibleTime(Long profileId, Integer time);

    boolean checkReferenceInAppointmentRequestDate(Long possibleDateId);

    boolean checkExistPossibleDate(Long profileId, String possibleDate);

    boolean checkExistPossibleTime(Long profileId, String possibleDate, Integer possibleTimeStart);
}
