package com.swyp3.babpool.domain.profile.application;

import com.swyp3.babpool.domain.profile.domain.PossibleDateAndTime;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class ProfileServiceImplTest {

    @Test
    void updatePossibleDateTime() {
        // given
        List<PossibleDateAndTime> existPossibleDateTimeLists = List.of(
                PossibleDateAndTime.builder()
                        .possibleDateId(1L)
                        .possibleDate("2024-03-14")
                        .possibleTimeIdList("111,222,333")
                        .possibleTimeList("1,2,3")
                        .build(),
                PossibleDateAndTime.builder()
                        .possibleDateId(1L)
                        .possibleDate("2024-03-15")
                        .possibleTimeIdList("444,555,666,777")
                        .possibleTimeList("1,2,3,7")
                        .build()
                ,
                PossibleDateAndTime.builder()
                        .possibleDateId(1L)
                        .possibleDate("2024-03-16")
                        .possibleTimeIdList("777")
                        .possibleTimeList("7")
                        .build()
        );
        Map<String, List<Integer>> requestPossibleDateTime = Map.of(
                "2024-03-14", List.of(2, 3, 4, 5),
                "2024-03-15", List.of(1, 3, 4, 5, 6),
                "2024-03-17", List.of(8, 9, 10)
        );

        // when
        Map<String, List<Integer>> deleteTarget = new HashMap<>(); // existPossibleDateTimeLists 에는 있지만 requestPossibleDateTime 에는 없는 것
        Map<String, List<Integer>> insertTarget = new HashMap<>(); // requestPossibleDateTime 에는 있지만 existPossibleDateTimeLists 에는 없는 것

        // existPossibleDateTimeLists 을 기준으로 순회하며 삭제 대상과 추가 대상을 구분
        for (PossibleDateAndTime existPossibleDateTimeList : existPossibleDateTimeLists) {
            String existPossibleDate = existPossibleDateTimeList.getPossibleDate();
            List<Integer> existPossibleTimeList = existPossibleDateTimeList.getPossibleTimeList();

            if (requestPossibleDateTime.containsKey(existPossibleDate)) { // 날짜는 같은데
                List<Integer> requestPossibleTime = requestPossibleDateTime.get(existPossibleDate);
                // requestPossibleTime 에는 있지만 existPossibleTimeList 에는 없는 것 : 추가 대상
                List<Integer> insertTimeList = new ArrayList<>();
                for (Integer time : requestPossibleTime) {
                    if (!existPossibleTimeList.contains(time)) {
                        insertTimeList.add(time);
                    }
                }
                insertTarget.put(existPossibleDate, insertTimeList);
                // existPossibleTimeList 에는 있지만 requestPossibleTime 에는 없는 것 : 삭제 대상
                List<Integer> deleteTimeList = new ArrayList<>();
                for (Integer time : existPossibleTimeList) {
                    if (!requestPossibleTime.contains(time)) {
                        deleteTimeList.add(time);
                    }
                }
                deleteTarget.put(existPossibleDate, deleteTimeList);

            } else { // 요청 받은 날짜에 기존에 존재하던 날짜가 없어졌다면 삭제 대상
                deleteTarget.put(existPossibleDate, existPossibleTimeList);
            }
        }

        // requestPossibleDateTime 을 기준으로 순회하며 추가 대상을 구분
        for (Map.Entry<String, List<Integer>> entry : requestPossibleDateTime.entrySet()) {
            String requestPossibleDate = entry.getKey();
            List<Integer> requestPossibleTime = entry.getValue();

            // existPossibleDateTimeLists 에는 없는 날짜(requestPossibleDate)는 추가 대상
            if (existPossibleDateTimeLists.stream().noneMatch(
                    existPossibleDateTimeList -> existPossibleDateTimeList.getPossibleDate().equals(requestPossibleDate))
            ) {
                insertTarget.put(requestPossibleDate, requestPossibleTime);
            }
        }

        // then
        log.info("deleteTarget: {}", deleteTarget);
        log.info("insertTarget: {}", insertTarget);
    }


    @Test
    void updatePossibleDateTime_PossibleDateAndTime() {
        // given
        List<PossibleDateAndTime> existPossibleDateTimeLists = List.of(
                PossibleDateAndTime.builder()
                        .possibleDateId(1L)
                        .possibleDate("2024-03-14")
                        .possibleTimeIdList("111,222,333")
                        .possibleTimeList("1,2,3")
                        .build(),
                PossibleDateAndTime.builder()
                        .possibleDateId(1L)
                        .possibleDate("2024-03-15")
                        .possibleTimeIdList("444,555,666,777")
                        .possibleTimeList("1,2,3,7")
                        .build()
                ,
                PossibleDateAndTime.builder()
                        .possibleDateId(1L)
                        .possibleDate("2024-03-16")
                        .possibleTimeIdList("777")
                        .possibleTimeList("7")
                        .build()
        );
        Map<String, List<Integer>> requestPossibleDateTime = Map.of(
                "2024-03-14", List.of(2, 3, 4, 5),
                "2024-03-15", List.of(1, 3, 4, 5, 6),
                "2024-03-17", List.of(8, 9, 10)
        );

        // when
        List<PossibleDateAndTime> deleteTargets = new ArrayList<>(); // existPossibleDateTimeLists 에는 있지만 requestPossibleDateTime 에는 없는 것
        Map<String, List<Integer>> insertTarget = new HashMap<>(); // requestPossibleDateTime 에는 있지만 existPossibleDateTimeLists 에는 없는 것

        for (PossibleDateAndTime exist : existPossibleDateTimeLists) {
            String existDate = exist.getPossibleDate();
            List<Integer> existTimes = exist.getPossibleTimeList();

            if (requestPossibleDateTime.containsKey(existDate)) {
                List<Integer> requestTimes = requestPossibleDateTime.get(existDate);


                // requestPossibleTime 에는 있지만 existPossibleTimeList 에는 없는 것 : 추가 대상
                List<Integer> insertTimeList = new ArrayList<>();
                for (Integer time : requestTimes) {
                    if (!existTimes.contains(time)) {
                        insertTimeList.add(time);
                    }
                }
                insertTarget.put(existDate, insertTimeList);

                // Determine times to delete: exist in existTimes but not in requestTimes
                List<Integer> timesToDelete = existTimes.stream()
                        .filter(time -> !requestTimes.contains(time))
                        .collect(Collectors.toList());

                if (!timesToDelete.isEmpty()) {
                    // Filter possibleTimeIdList based on timesToDelete for accurate ID mapping
                    List<Long> timeIdsToDelete = filterTimeIds(exist.getPossibleTimeIdList(), existTimes, timesToDelete);
                    deleteTargets.add(new PossibleDateAndTime(exist.getPossibleDateId(), existDate, timeIdsToDelete, timesToDelete));
                }
            } else {
                // The entire date is missing in the request, mark all times for deletion
                deleteTargets.add(exist);
            }
        }

        // requestPossibleDateTime 을 기준으로 순회하며 추가 대상을 구분
        for (Map.Entry<String, List<Integer>> entry : requestPossibleDateTime.entrySet()) {
            String requestPossibleDate = entry.getKey();
            List<Integer> requestPossibleTime = entry.getValue();

            // existPossibleDateTimeLists 에는 없는 날짜(requestPossibleDate)는 추가 대상
            if(existPossibleDateTimeLists.stream().noneMatch(
                    existPossibleDateTimeList -> existPossibleDateTimeList.getPossibleDate().equals(requestPossibleDate))
            ){
                insertTarget.put(requestPossibleDate, requestPossibleTime);
            }
        }

        // then
        log.info("deleteTarget: {}", deleteTargets.toString());
        log.info("insertTarget: {}", insertTarget);

    }

    private List<Long> filterTimeIds(List<Long> timeIds, List<Integer> existTimes, List<Integer> timesToDelete) {
        List<Long> filteredTimeIds = new ArrayList<>();
        for (int i = 0; i < existTimes.size(); i++) {
            if (timesToDelete.contains(existTimes.get(i))) {
                filteredTimeIds.add(timeIds.get(i));
            }
        }
        return filteredTimeIds;
    }
}