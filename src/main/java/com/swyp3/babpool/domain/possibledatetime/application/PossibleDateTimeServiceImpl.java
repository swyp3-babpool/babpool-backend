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

    @Transactional
    @Override
    public PossibleDateTime throwExceptionIfAppointmentAlreadyAcceptedAtSameTime(Long targetProfileId, Long possibleDateTimeId, LocalDateTime possibleDateTime) {
        PossibleDateTime possibleDateTimeEntity = possibleDateTimeRepository.findByProfileIdAndDateTimeForUpdate(
                targetProfileId, possibleDateTimeId).orElseThrow(
                () -> new PossibleDateTimeException(PossibleDateTimeErrorCode.POSSIBLE_DATETIME_NOT_FOUND, "[Error Log] 밥약 가능한 일정이 존재하지 않습니다.")
        );

        if (possibleDateTimeEntity.getPossibleDateTimeStatus().equals(PossibleDateTimeStatusType.RESERVED.getStatus())){
            throw new PossibleDateTimeException(PossibleDateTimeErrorCode.POSSIBLE_DATETIME_ALREADY_RESERVED, "이미 예약된 시간대 입니다. 다른 시간대를 다시 선택해주세요.");
        }

        return possibleDateTimeEntity;
    }

    @Transactional
    @Override
    public void changeStatusAsReserved(Long possibleDateTimeId) {
        int updatedRows = possibleDateTimeRepository.updatePossibleDateTimeStatus(possibleDateTimeId, PossibleDateTimeStatusType.RESERVED.getStatus());
        if (updatedRows != 1){
            throw new PossibleDateTimeException(PossibleDateTimeErrorCode.POSSIBLE_DATETIME_STATUS_UPDATE_FAILED, "밥약 가능한 일정 상태 변경에 실패하였습니다.");
        }
    }
}
