package com.swyp3.babpool.domain.possibledatetime.application;

import com.swyp3.babpool.domain.possibledatetime.api.request.PossibleDateTimeUpdateRequest;
import com.swyp3.babpool.domain.possibledatetime.application.response.PossibleDateTimeResponse;
import com.swyp3.babpool.domain.possibledatetime.dao.PossibleDateTimeRepository;
import com.swyp3.babpool.domain.possibledatetime.domain.PossibleDateTime;
import com.swyp3.babpool.domain.possibledatetime.domain.PossibleDateTimeStatusType;
import com.swyp3.babpool.domain.possibledatetime.exception.PossibleDateTimeException;
import com.swyp3.babpool.domain.possibledatetime.exception.errorcode.PossibleDateTimeErrorCode;
import com.swyp3.babpool.domain.profile.exception.ProfileException;
import com.swyp3.babpool.domain.profile.exception.errorcode.ProfileErrorCode;
import com.swyp3.babpool.global.tsid.TsidKeyGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class PossibleDateTimeServiceImpl implements PossibleDateTimeService{

    private final PossibleDateTimeRepository possibleDateTimeRepository;
    private final TsidKeyGenerator tsidKeyGenerator;


    @Override
    public PossibleDateTime throwExceptionIfAppointmentAlreadyAcceptedAtSameTime(Long targetUserId, Long possibleDateTimeId, LocalDateTime possibleDateTime) {
        PossibleDateTime possibleDateTimeEntity = possibleDateTimeRepository.findByUserIdAndDateTimeForUpdate(
                targetUserId, possibleDateTimeId).orElseThrow(
                () -> new PossibleDateTimeException(PossibleDateTimeErrorCode.POSSIBLE_DATETIME_NOT_FOUND, "조회된 PossibleDateTime 이 존재하지 않습니다.")
        );
        if (possibleDateTimeEntity.getPossibleDateTimeStatus() == PossibleDateTimeStatusType.RESERVED){
            throw new PossibleDateTimeException(PossibleDateTimeErrorCode.POSSIBLE_DATETIME_ALREADY_RESERVED, "조회된 PossibleDateTime의 status가 RESERVED 입니다.");
        }

        return possibleDateTimeEntity;
    }


    @Override
    public boolean changeStatusAsReserved(Long possibleDateTimeId) {
        int updatedRows = possibleDateTimeRepository.updatePossibleDateTimeStatus(possibleDateTimeId, PossibleDateTimeStatusType.RESERVED);
        if (updatedRows != 1){
            throw new PossibleDateTimeException(PossibleDateTimeErrorCode.POSSIBLE_DATETIME_STATUS_UPDATE_FAILED, "밥약 가능한 일정 상태 변경에 실패하였습니다.");
        }
        return true;
    }

    @Override
    public List<PossibleDateTimeResponse> updatePossibleDateTime(Long userId, PossibleDateTimeUpdateRequest possibleDateTimeUpdateRequest) {
        
        // 일정 제거 : possibleDateTimeStatus 가 RESERVED 인 경우는 제외하고 삭제
        if(!possibleDateTimeUpdateRequest.getPossibleDateTimeDelList().isEmpty()) {
            possibleDateTimeRepository.deletePossibleDateTimeWhereStatusIsNotReserved(userId, possibleDateTimeUpdateRequest.getPossibleDateTimeDelList());
        }
        // 추가
        if(!possibleDateTimeUpdateRequest.getPossibleDateTimeAddList().isEmpty()) {
            possibleDateTimeRepository.savePossibleDateTimeList(possibleDateTimeUpdateRequest.getPossibleDateTimeAddList().stream()
                    .map(datetime -> PossibleDateTime.builder().possibleDateTimeId(tsidKeyGenerator.generateTsid())
                            .userId(userId)
                            .possibleDateTime(datetime)
                            .possibleDateTimeStatus(PossibleDateTimeStatusType.AVAILABLE)
                            .build())
                    .toList());
        }
        // 해당 사용자가 활성화한 모든 가능한 일정 조회해서 응답
        return possibleDateTimeRepository.findAllByUserIdWhereFromThisMonth(userId)
                .stream()
                .map(PossibleDateTimeResponse::from)
                .toList();
    }

    @Override
    public List<PossibleDateTimeResponse> getPossibleDateTimeList(Long userId) {
        List<PossibleDateTime> allByUserId = possibleDateTimeRepository.findAllByUserId(userId);
        if (allByUserId.isEmpty()) {
            throw new PossibleDateTimeException(PossibleDateTimeErrorCode.POSSIBLE_DATETIME_NOT_FOUND, "조회된 PossibleDateTime 이 존재하지 않습니다.");
        }
        return allByUserId.stream()
                .map(PossibleDateTimeResponse::from)
                .toList();
    }

    @Override
    public LocalDateTime getPossibleDateTimeByDateTimeId(Long possibleDateTimeId) {
        return possibleDateTimeRepository.findById(possibleDateTimeId).orElseThrow(
                () -> new PossibleDateTimeException(PossibleDateTimeErrorCode.POSSIBLE_DATETIME_NOT_FOUND, "조회된 PossibleDateTime 이 존재하지 않습니다.")
        ).getPossibleDateTime();
    }

    @Override
    public Long findPossibleDateTimeIdByReceiverAndDateTimeAndStatus(Long receiverUserId, LocalDateTime possibleDateTime, PossibleDateTimeStatusType possibleDateTimeStatus) {
        return possibleDateTimeRepository.findByUserIdAndDateTimeWhereStatus(receiverUserId, possibleDateTime, possibleDateTimeStatus).orElseThrow(
                () -> new PossibleDateTimeException(PossibleDateTimeErrorCode.POSSIBLE_DATETIME_NOT_FOUND, "조회된 PossibleDateTime 이 존재하지 않습니다.")
        ).getPossibleDateTimeId();
    }

    private void validateRequestPossibleDateTime(Map<String, List<Integer>> possibleDateMap) {
        if(possibleDateMap.isEmpty()){
            throw new ProfileException(ProfileErrorCode.PROFILE_POSSIBLE_DATE_ERROR,"가능한 날짜와 시간을 최소 1개 이상 선택해주세요.");
        }
        // time : 8 ~ 22 only
        possibleDateMap.values().stream()
                .flatMap(List::stream)
                .forEach(time -> {
                    if(time < 8 || time > 22){
                        throw new ProfileException(ProfileErrorCode.PROFILE_POSSIBLE_DATE_ERROR,"가능한 시간은 8시부터 22시까지만 선택 가능합니다.");
                    }
                });
    }
}
