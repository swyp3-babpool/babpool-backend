package com.swyp3.babpool.domain.possibledatetime.dao;

import com.swyp3.babpool.domain.possibledatetime.domain.PossibleDateInsertDto;
import com.swyp3.babpool.domain.possibledatetime.domain.PossibleDateTime;
import com.swyp3.babpool.domain.profile.domain.PossibleDateAndTime;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Mapper
public interface PossibleDateTimeRepository {
    List<PossibleDateAndTime> findAllPossibleDateAndTimeByProfileIdAndNowDateWithoutAcceptOrDone(Long profileId);

    void deletePossibleDate(@Param("profileId") Long profileId, @Param("dateId") Long dateId);

    void deletePossibleTime(@Param("dateId") Long dateId, @Param("timeId") Long timeId);

    void insertPossibleDate(PossibleDateInsertDto possibleDateInsertDto);

    void insertPossibleTime(@Param("dateId") Long dateId, @Param("time") Integer time);

    boolean checkReferenceInAppointmentRequestDate(Long possibleDateId);

    Long checkExistPossibleDate(Long profileId, String possibleDate);

    boolean checkExistPossibleTime(Long profileId, String possibleDate, Integer possibleTimeStart);

    Optional<PossibleDateTime> findByProfileIdAndDateTimeForUpdate(Long targetProfileId, Long possibleDateTimeId);

    int updatePossibleDateTimeStatus(Long possibleDateTimeId, String status);
}
