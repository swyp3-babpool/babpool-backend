package com.swyp3.babpool.domain.profile.application.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
@ToString
@Getter
public class ProfilePagingResponse {

    private Long profileId;
    private Long userId;
    private String profileImageUrl;
    private String profileIntro;
    private String profileContents;
    private LocalDateTime profileModifyDate; // t_profile 테이블의 profile_modify_date
    private String keywordIdWithNameString; // t_keyword 테이블의 keyword_id와 keyword_name을 합친 문자열
    private String userGrade; // t_user_account 테이블의 user_grade

    @Builder
    public ProfilePagingResponse(Long profileId, Long userId, String profileImageUrl, String profileIntro, String profileContents, LocalDateTime profileModifyDate, String keywordIdWithNameString, String userGrade) {
        this.profileId = profileId;
        this.userId = userId;
        this.profileImageUrl = profileImageUrl;
        this.profileIntro = profileIntro;
        this.profileContents = profileContents;
        this.profileModifyDate = profileModifyDate;
        this.keywordIdWithNameString = keywordIdWithNameString;
        this.userGrade = userGrade;
    }


}

