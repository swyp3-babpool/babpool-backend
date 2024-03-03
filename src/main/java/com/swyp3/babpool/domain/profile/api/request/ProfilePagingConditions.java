package com.swyp3.babpool.domain.profile.api.request;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class ProfilePagingConditions {

    private String search;
    private List<String> userGrades;
    private List<String> keywords;

    @Builder
    public ProfilePagingConditions(String search, List<String> userGrades, List<String> keywords) {
        this.search = search;
        this.userGrades = userGrades;
        this.keywords = keywords;
    }
}
