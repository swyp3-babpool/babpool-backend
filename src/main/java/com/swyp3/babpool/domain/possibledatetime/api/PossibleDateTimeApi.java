package com.swyp3.babpool.domain.possibledatetime.api;

import com.swyp3.babpool.domain.possibledatetime.api.request.PossibleDateTimeUpdateRequest;
import com.swyp3.babpool.domain.possibledatetime.application.PossibleDateTimeService;
import com.swyp3.babpool.domain.possibledatetime.application.response.PossibleDateTimeResponse;
import com.swyp3.babpool.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
public class PossibleDateTimeApi {

    private final PossibleDateTimeService possibleDateTimeService;

//    /**
//     * 밥약 가능한 일정 조회 API
//     * @param userId : 조회할 사용자의 userId
//     * @return : 조회한 사용자의 이달부터의 모든 일정 리스트를 반환한다.
//     */
//    @Parameter(name = "userId", description = "조회할 사용자의 userId", required = true, example = "598129329137778868")
//    @GetMapping("/api/possible/datetime/{userId}")
//    public ApiResponse<List<PossibleDateTimeResponse>> getPossibleDateTimeList(@PathVariable @Positive(message = "Must be positive") Long userId){
//        return ApiResponse.ok(possibleDateTimeService.getPossibleDateTimeList(userId));
//    }

    /**
     * 밥약 가능한 일정 조회 API
     * @param profileId : 조회할 사용자의 profileId
     * @return : 조회한 사용자의 이달부터의 모든 일정 리스트를 반환한다.
     */
    @Parameter(name = "profileId", description = "조회할 사용자의 profileId", required = true, example = "598335473802281880")
    @GetMapping("/api/possible/datetime/{profileId}")
    public ApiResponse<List<PossibleDateTimeResponse>> getPossibleDateTimeListByProfileId(@PathVariable @Positive(message = "Must be positive") Long profileId){
        return ApiResponse.ok(possibleDateTimeService.getPossibleDateTimeListByProfileId(profileId));
    }

    /**
     * 밥약 가능한 일정 수정(추가 및 삭제) API
     * @param userId : 요청자의 userId, 쿠키의 JWT 토큰에서 추출
     * @param possibleDateTimeUpdateRequest : possibleDateTimeAddList, possibleDateTimeDelList에 추가 및 삭제할 LocalDateTime 타입의 일정이 요청된다.
     * @return {@code List<PossibleDateTimeResponse>} : 요청자의 이달부터의 모든 일정 리스트를 반환한다. PossibleDateTimeResponse 클래스는 PossibleDateTime에서 사용자 Id는 제외되어있다.
     */
    @PostMapping("/api/possible/datetime")
    public ApiResponse<List<PossibleDateTimeResponse>> updatePossibleDateTime(@RequestAttribute(value = "userId") Long userId,
                                                                                         @RequestBody PossibleDateTimeUpdateRequest possibleDateTimeUpdateRequest){
        return ApiResponse.ok(possibleDateTimeService.updatePossibleDateTime(userId, possibleDateTimeUpdateRequest));
    }
}
