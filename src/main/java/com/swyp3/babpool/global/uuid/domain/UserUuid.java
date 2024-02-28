package com.swyp3.babpool.global.uuid.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class UserUuid {

    private Long userId;
    private byte[] userUuid;

    @Builder
    public UserUuid(Long userId, byte[] userUuid) {
        this.userId = userId;
        this.userUuid = userUuid;
    }
}
