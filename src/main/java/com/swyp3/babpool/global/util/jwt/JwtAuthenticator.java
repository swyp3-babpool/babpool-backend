package com.swyp3.babpool.global.util.jwt;

import com.swyp3.babpool.global.uuid.dao.UserUuidRepository;
import com.swyp3.babpool.global.uuid.exception.UuidErrorCode;
import com.swyp3.babpool.global.uuid.exception.UuidException;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticator {

    private final JwtTokenizer jwtTokenizer;
    private final UserUuidRepository userUuidRepository;

    public Claims authenticate(String accessToken) {
        return jwtTokenizer.parseAccessToken(accessToken);
    }

    public Long jwtTokenUserIdResolver(String userUuid) {
        return userUuidRepository.findByUserUuId(userUuid).orElseThrow(
                    () -> new UuidException(UuidErrorCode.NOT_FOUND_USER_UUID,
                            "Not found user id with uuid, while JwtAuthenticator request to UserUuidRepository"))
                .getUserId();
    }
}
