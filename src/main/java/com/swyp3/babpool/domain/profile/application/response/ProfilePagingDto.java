package com.swyp3.babpool.domain.profile.application.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
@ToString
@Getter
public class ProfilePagingDto {

    private Long profileId;
    private Long userId;
    private String profileImageUrl;
    private String profileIntro;
    private String profileContents;
    private LocalDateTime profileModifyDate; // t_profile 테이블의 profile_modify_date
    private String keywordNamesConcat; // t_keyword 테이블의 keyword_name (콤마로 구분된 문자열)
    private String userGrade; // t_user_account 테이블의 user_grade
    private String userNickname; // t_user_account 테이블의 user_nickname

    @Builder
    public ProfilePagingDto(Long profileId, Long userId, String profileImageUrl, String profileIntro, String profileContents, LocalDateTime profileModifyDate, String keywordNamesConcat, String userGrade, String userNickname) {
        this.profileId = profileId;
        this.userId = userId;
        this.profileImageUrl = profileImageUrl;
        this.profileIntro = profileIntro;
        this.profileContents = profileContents;
        this.profileModifyDate = profileModifyDate;
        this.keywordNamesConcat = keywordNamesConcat;
        this.userGrade = userGrade;
        this.userNickname = userNickname;
    }
}

