package com.swyp3.babpool.domain.possibledatetime.application;

import com.swyp3.babpool.domain.possibledatetime.dao.PossibleDateTimeRepository;
import com.swyp3.babpool.domain.possibledatetime.domain.PossibleDateTime;
import com.swyp3.babpool.domain.possibledatetime.domain.PossibleDateTimeStatusType;
import com.swyp3.babpool.domain.possibledatetime.exception.PossibleDateTimeException;
import com.swyp3.babpool.domain.possibledatetime.exception.errorcode.PossibleDateTimeErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class PossibleDateTimeServiceImpl implements PossibleDateTimeService{

    private final PossibleDateTimeRepository possibleDateTimeRepository;


    @Override
    public PossibleDateTime throwExceptionIfAppointmentAlreadyAcceptedAtSameTime(Long targetProfileId, Long possibleDateTimeId, LocalDateTime possibleDateTime) {
        PossibleDateTime possibleDateTimeEntity = possibleDateTimeRepository.findByProfileIdAndDateTimeForUpdate(
                targetProfileId, possibleDateTimeId).orElseThrow(
                () -> new PossibleDateTimeException(PossibleDateTimeErrorCode.POSSIBLE_DATETIME_NOT_FOUND, "조회된 PossibleDateTime 이 존재하지 않습니다.")
        );
        if (possibleDateTimeEntity.getPossibleDateTimeStatus().equals(PossibleDateTimeStatusType.RESERVED.getStatus())){
            throw new PossibleDateTimeException(PossibleDateTimeErrorCode.POSSIBLE_DATETIME_ALREADY_RESERVED, "조회된 PossibleDateTime의 status가 RESERVED 입니다.");
        }

        return possibleDateTimeEntity;
    }


    @Override
    public boolean changeStatusAsReserved(Long possibleDateTimeId) {
        int updatedRows = possibleDateTimeRepository.updatePossibleDateTimeStatus(possibleDateTimeId, PossibleDateTimeStatusType.RESERVED.getStatus());
        if (updatedRows != 1){
            throw new PossibleDateTimeException(PossibleDateTimeErrorCode.POSSIBLE_DATETIME_STATUS_UPDATE_FAILED, "밥약 가능한 일정 상태 변경에 실패하였습니다.");
        }
        return true;
    }
}
