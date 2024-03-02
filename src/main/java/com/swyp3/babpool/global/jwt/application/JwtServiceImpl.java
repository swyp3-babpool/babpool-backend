package com.swyp3.babpool.global.jwt.application;

import com.swyp3.babpool.global.jwt.JwtTokenizer;
import com.swyp3.babpool.global.jwt.application.response.JwtPairDto;
import com.swyp3.babpool.global.jwt.exception.BabpoolJwtException;
import com.swyp3.babpool.global.jwt.exception.errorcode.JwtExceptionErrorCode;
import com.swyp3.babpool.infra.redis.dao.TokenRedisRepository;
import com.swyp3.babpool.infra.redis.domain.TokenForRedis;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService{

    private final TokenRedisRepository tokenRepository;
    private final JwtTokenizer jwtTokenizer;

    @Value("${property.jwt.refreshTokenExpireDays}")
    private Integer refreshExpire;

    @Override
    public JwtPairDto createJwtPair(String userUUID, List roles) {
        JwtPairDto jwtPairDto = JwtPairDto.builder()
                .accessToken(jwtTokenizer.createAccessToken(userUUID, roles))
                .refreshToken(jwtTokenizer.createRefreshToken(userUUID, roles))
                .build();
        tokenRepository.save(TokenForRedis.builder()
                .refreshToken(jwtPairDto.getRefreshToken())
                .refreshExpire(refreshExpire)
                .userUUID(userUUID)
                .build());
        return jwtPairDto;
    }

    @Override
    public String extendLoginState(String refreshToken) {
        TokenForRedis tokenObject = tokenRepository.findById(refreshToken)
                .orElseThrow(() -> new BabpoolJwtException(JwtExceptionErrorCode.REFRESH_TOKEN_NOT_FOUND,
                        "refresh token not found in redis, while extending login state."));

        Claims claims = jwtTokenizer.parseRefreshToken(refreshToken);
        String userUUID = claims.getSubject();

        // TODO : Redis 에서 꺼낸 tokenObject 의 userUUID 와 request refresh token claims 의 userUUID 가 다른지 까지 검증해야 할까요?
        if (!tokenObject.getUserUUID().equals(userUUID)) {
            throw new BabpoolJwtException(JwtExceptionErrorCode.REFRESH_TOKEN_NOT_SAME_USER,
                    "user uuid in request refresh token and redis refresh token are not same, while extending login state.");
        }

        return jwtTokenizer.createAccessToken(userUUID, claims.get("roles", List.class));
    }

    @Override
    public void logout(String refreshTokenFromCookie) {
        tokenRepository.findById(refreshTokenFromCookie)
                .orElseThrow(() -> new BabpoolJwtException(JwtExceptionErrorCode.REFRESH_TOKEN_NOT_FOUND,
                "refresh token not found in redis, while logout"));
        tokenRepository.deleteById(refreshTokenFromCookie);
    }
}
