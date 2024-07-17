package com.swyp3.babpool.domain.possibledatetime.dao;

import com.swyp3.babpool.domain.possibledatetime.domain.PossibleDateTime;
import com.swyp3.babpool.domain.possibledatetime.domain.PossibleDateTimeStatusType;
import com.swyp3.babpool.domain.profile.domain.PossibleDateAndTime;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Mapper
public interface PossibleDateTimeRepository {
    List<PossibleDateAndTime> findAllPossibleDateAndTimeByProfileIdAndNowDateWithoutAcceptOrDone(Long profileId);

//    void deletePossibleDate(@Param("profileId") Long profileId, @Param("dateId") Long dateId);
//
//    void deletePossibleTime(@Param("dateId") Long dateId, @Param("timeId") Long timeId);
//
//    void insertPossibleDate(PossibleDateInsertDto possibleDateInsertDto);
//
//    void insertPossibleTime(@Param("dateId") Long dateId, @Param("time") Integer time);
    boolean checkReferenceInAppointmentRequestDate(Long possibleDateId);

    Long checkExistPossibleDate(Long profileId, String possibleDate);

    boolean checkExistPossibleTime(Long profileId, String possibleDate, Integer possibleTimeStart);

    Optional<PossibleDateTime> findById(Long possibleDateTimeId);

    void save(PossibleDateTime possibleDateTime);

    Optional<PossibleDateTime> findByUserIdAndDateTimeForUpdate(@Param("userId") Long receiverUserId, @Param("possibleDateTimeId") Long possibleDateTimeId);

    Optional<PossibleDateTime> findByUserIdAndDateTime(@Param("userId") Long receiverUserId, @Param("possibleDateTimeId") Long possibleDateTimeId);
    // 테스트 코드 작성 완료
    int updatePossibleDateTimeStatus(Long possibleDateTimeId, PossibleDateTimeStatusType status);

    // 테스트 코드 작성 완료
    List<PossibleDateTime> findAllByUserId(Long userId);

    // 테스트 코드 작성 완료
    int deletePossibleDateTimeWhereStatusIsNotReserved(Long userId, List<LocalDateTime> possibleDateTimeDelList);

    // 테스트 코드 작성 완료
    void savePossibleDateTimeList(List<PossibleDateTime> possibleDateTimeList);

    List<PossibleDateTime> findAllByUserIdWhereFromThisMonth(Long userId);
}
