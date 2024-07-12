package com.swyp3.babpool.global.uuid.application;

import com.swyp3.babpool.global.uuid.dao.UserUuidRepository;
import com.swyp3.babpool.global.uuid.domain.UserUuid;
import com.swyp3.babpool.global.uuid.exception.UuidErrorCode;
import com.swyp3.babpool.global.uuid.exception.UuidException;
import com.swyp3.babpool.global.uuid.util.UuidResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * UuidServiceV7, UUID 버전 7을 사용하여 식별값을 생성하거나 변환하는 기능을 제공하는 서비스 클래스입니다.
 * @deprecated TSID 도입으로 인해 UUID 를 더이상 사용하지 않습니다. 해당 클래스는 삭제될 예정입니다.
 */
@Deprecated
@Primary
@Service
@RequiredArgsConstructor
public class UuidServiceV7 implements UuidService{

    private final UuidResolver uuidResolver;
    private final UserUuidRepository uuidRepository;

    @Override
    public UUID createUuid(Long userId) {
        // UUID 생성
        UUID targetUuid = uuidResolver.generateUuid();
        // Parse UUID to Byte[]
        byte[] targetBytes = uuidResolver.parseUuidToBytes(targetUuid);
        // DB에 UUID 저장
        uuidRepository.save(UserUuid.builder()
                .userId(userId)
                .userUuid(targetBytes)
                .build());
        // UUID 타입 반환
        return targetUuid;
    }

    @Override
    public UUID getUuidByUserId(Long userId) {
        byte[] resultUuidBytes = uuidRepository.findByUserId(userId).orElseThrow(
                () -> new UuidException(UuidErrorCode.NOT_FOUND_USER_UUID,
                        "Not found user uuid with user id, while UuidServiceV7.getUuidByUserId() request to UserUuidRepository"))
                .getUserUuid();
        return uuidResolver.parseBytesToUuid(resultUuidBytes);
    }

    @Override
    public Long getUserIdByUuid(String userUuidOfStringType) {
        return uuidRepository.findByUserUuIdBytes(uuidResolver.parseUuidToBytes(UUID.fromString(userUuidOfStringType))).orElseThrow(
                        () -> new UuidException(UuidErrorCode.NOT_FOUND_USER_UUID,
                                "Not found user id with uuid, while UuidService request to UserUuidRepository"))
                .getUserId();
    }


}
