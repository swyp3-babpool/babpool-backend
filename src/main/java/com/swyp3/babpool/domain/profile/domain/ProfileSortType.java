package com.swyp3.babpool.domain.profile.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ProfileSortType {

    NewestPofile("profile_modify_date"),
    NickName("user_nick_name"),
    NewestReview("review_create_date")
    ;

    private final String columnName;
}
