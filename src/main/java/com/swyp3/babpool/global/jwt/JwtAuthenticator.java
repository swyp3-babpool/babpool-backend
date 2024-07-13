package com.swyp3.babpool.global.jwt;

import com.swyp3.babpool.global.uuid.dao.UserUuidRepository;
import com.swyp3.babpool.global.uuid.exception.UuidErrorCode;
import com.swyp3.babpool.global.uuid.exception.UuidException;
import com.swyp3.babpool.global.uuid.util.UuidResolver;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtAuthenticator {

    private final JwtTokenizer jwtTokenizer;
    private final UuidResolver uuidResolver;
    private final UserUuidRepository userUuidRepository;

    public Claims authenticate(String accessToken) {
        return jwtTokenizer.parseAccessToken(accessToken);
    }

    public Long jwtTokenUserUuidToUserIdResolver(String userUuid) {
        return userUuidRepository.findByUserUuIdBytes(uuidResolver.parseUuidToBytes(UUID.fromString(userUuid))).orElseThrow(
                    () -> new UuidException(UuidErrorCode.NOT_FOUND_USER_UUID,
                            "Not found user id with uuid, while JwtAuthenticator request to UserUuidRepository"))
                .getUserId();
    }

    public Long jwtRefreshTokenToUserIdResolver(String refreshToken) {
        Claims claims = jwtTokenizer.parseRefreshToken(refreshToken);
        return Long.valueOf(claims.getSubject());
    }



}
