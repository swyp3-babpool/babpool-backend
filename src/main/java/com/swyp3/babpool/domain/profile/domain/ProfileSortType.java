package com.swyp3.babpool.domain.profile.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ProfileSortType {

    Newest("profile_modify_date"),
    Name("user_name")
    ;

    private final String columnName;
}
