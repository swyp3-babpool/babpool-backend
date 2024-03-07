package com.swyp3.babpool.domain.profile.application.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@ToString
@Getter
public class ProfilePagingResponse {

    private Long profileId;
    private Long userId;
    private String profileImageUrl;
    private String profileIntro;
    private String profileContents;
    private LocalDateTime profileModifyDate;
    private List<String> keywordNameList;
    private String userGrade;
    private String userNickname;

    @Builder
    public ProfilePagingResponse(Long profileId, Long userId, String profileImageUrl, String profileIntro, String profileContents, LocalDateTime profileModifyDate, List<String> keywordNameList, String userGrade, String userNickname) {
        this.profileId = profileId;
        this.userId = userId;
        this.profileImageUrl = profileImageUrl;
        this.profileIntro = profileIntro;
        this.profileContents = profileContents;
        this.profileModifyDate = profileModifyDate;
        this.keywordNameList = keywordNameList;
        this.userGrade = userGrade;
        this.userNickname = userNickname;
    }

    public static ProfilePagingResponse of(ProfilePagingDto profilePagingDto) {
        return ProfilePagingResponse.builder()
                .profileId(profilePagingDto.getProfileId())
                .userId(profilePagingDto.getUserId())
                .profileImageUrl(profilePagingDto.getProfileImageUrl())
                .profileIntro(profilePagingDto.getProfileIntro())
                .profileContents(profilePagingDto.getProfileContents())
                .profileModifyDate(profilePagingDto.getProfileModifyDate())
                .keywordNameList(Arrays.stream(profilePagingDto.getKeywordNamesConcat().split(",")).toList())
                .userGrade(profilePagingDto.getUserGrade())
                .userNickname(profilePagingDto.getUserNickname())
                .build();
    }

}
