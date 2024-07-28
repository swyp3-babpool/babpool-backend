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

    // 테스트 코드 작성 완료
    Optional<PossibleDateTime> findById(Long possibleDateTimeId);

    // 테스트 코드 작성 완료
    List<PossibleDateTime> findAllByUserIdWhereFromThisMonth(Long userId);

    // 테스트 코드 작성 완료
    Optional<PossibleDateTime> findByUserIdAndDateTimeForUpdate(@Param("userId") Long receiverUserId, @Param("possibleDateTimeId") Long possibleDateTimeId);

    // 테스트 코드 작성 완료
    Optional<PossibleDateTime> findByUserIdAndDateTimeId(@Param("userId") Long receiverUserId, @Param("possibleDateTimeId") Long possibleDateTimeId);

    // 테스트 코드 작성 완료
    int updatePossibleDateTimeStatus(Long possibleDateTimeId, PossibleDateTimeStatusType status);

    // 테스트 코드 작성 완료
    List<PossibleDateTime> findAllByUserId(Long userId);

    // 테스트 코드 작성 완료
    int deletePossibleDateTimeWhereStatusIsNotReserved(Long userId, List<LocalDateTime> possibleDateTimeDelList);

    // 테스트 코드 작성 완료
    void save(PossibleDateTime possibleDateTime);

    // 테스트 코드 작성 완료
    void savePossibleDateTimeList(List<PossibleDateTime> possibleDateTimeList);


    @Deprecated
    List<PossibleDateAndTime> findAllPossibleDateAndTimeByProfileIdAndNowDateWithoutAcceptOrDone(Long profileId);
    @Deprecated
    boolean checkReferenceInAppointmentRequestDate(Long possibleDateId);
    @Deprecated
    Long checkExistPossibleDate(Long profileId, String possibleDate);
    @Deprecated
    boolean checkExistPossibleTime(Long profileId, String possibleDate, Integer possibleTimeStart);

    Optional<PossibleDateTime> findByUserIdAndDateTimeWhereStatus(Long receiverUserId, LocalDateTime possibleDateTime, PossibleDateTimeStatusType possibleDateTimeStatus);

    List<PossibleDateTime> findAllByProfileIdWhereFromThisMonth(Long profileId);
}
