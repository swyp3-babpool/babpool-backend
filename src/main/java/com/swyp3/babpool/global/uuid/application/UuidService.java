package com.swyp3.babpool.global.uuid.application;

import java.util.UUID;

public interface UuidService {

    UUID createUuid(Long userId);
    UUID getUuidByUserId(Long userId);
    Long getUserIdByUuid(String userUuidOfStringType);
}
