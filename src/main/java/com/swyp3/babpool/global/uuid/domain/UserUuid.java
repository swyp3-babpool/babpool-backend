package com.swyp3.babpool.global.uuid.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class UserUuid {

    private Long UserId;
    private String UserUuid;

    @Builder
    public UserUuid(Long UserId, String UserUuid) {
        this.UserId = UserId;
        this.UserUuid = UserUuid;
    }
}
