package com.swyp3.babpool.domain.profile.exception.errorcode;

import com.swyp3.babpool.global.common.exception.errorcode.CustomErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ProfileErrorCode implements CustomErrorCode {

    PROFILE_LIST_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "프로필 리스트 조회 중 오류가 발생했습니다."),
    PROFILE_TARGET_PROFILE_ERROR(HttpStatus.BAD_REQUEST,"프로필 상세 조회 중 오류가 발생했습니다.");
    private final HttpStatus httpStatus;
    private final String message;
}
