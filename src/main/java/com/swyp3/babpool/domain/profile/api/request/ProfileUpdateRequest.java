package com.swyp3.babpool.domain.profile.api.request;

import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProfileUpdateRequest {
    private String userNickName;
    private String userGrade;
    private String profileIntro;
    private String profileContents;
    private String profileContactPhone;
    private String profileContactChat;
    private List<Long> keywords;
    private Map<String, List<Integer>> possibleDate;
}
