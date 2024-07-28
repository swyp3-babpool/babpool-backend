package com.swyp3.babpool.global.jwt;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * JWT Tokenizer를 통해 토큰을 파싱하고, 클레임을 반환한다.
 * [240719] UUID를 사용하지 않게 되어, 관련 메서드 주석 처리.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticator {

    private final JwtTokenizer jwtTokenizer;

    /**
     * Access Token을 검증하고, 클레임을 반환한다.
     * @param accessToken
     * @return
     */
    public Claims authenticate(String accessToken) {
        return jwtTokenizer.parseAccessToken(accessToken);
    }

//    public Long jwtTokenUserUuidToUserIdResolver(String userUuid) {
//        return userUuidRepository.findByUserUuIdBytes(uuidResolver.parseUuidToBytes(UUID.fromString(userUuid))).orElseThrow(
//                    () -> new UuidException(UuidErrorCode.NOT_FOUND_USER_UUID,
//                            "Not found user id with uuid, while JwtAuthenticator request to UserUuidRepository"))
//                .getUserId();
//    }

    /**
     * Refresh Token을 검증하고, 클레임에 저장된 Subject를 반환한다.
     * @param refreshToken
     * @return
     */
    public Long jwtRefreshTokenToUserIdResolver(String refreshToken) {
        Claims claims = jwtTokenizer.parseRefreshToken(refreshToken);
        return Long.valueOf(claims.getSubject());
    }



}
