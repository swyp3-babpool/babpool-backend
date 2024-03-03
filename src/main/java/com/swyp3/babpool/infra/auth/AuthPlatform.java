package com.swyp3.babpool.infra.auth;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;

@Getter
@JsonDeserialize(using = PlatformDeserializer.class)
public enum AuthPlatform {
    KAKAO,
    GOOGLE
}
