package com.swyp3.babpool.domain.profile.api.request;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class ProfileListRequest {

    private String searchTerm;
    private List<String> keywords;

    @Builder
    public ProfileListRequest(String searchTerm, List<String> keywords) {
        this.searchTerm = searchTerm;
        this.keywords = keywords;
    }
}
