package com.swyp3.babpool.global.jwt.application;

import com.swyp3.babpool.global.jwt.JwtTokenizer;
import com.swyp3.babpool.global.jwt.application.response.JwtPairDto;
import com.swyp3.babpool.global.jwt.exception.BabpoolJwtException;
import com.swyp3.babpool.global.jwt.exception.errorcode.JwtExceptionErrorCode;
import com.swyp3.babpool.infra.redis.dao.TokenRedisRepository;
import com.swyp3.babpool.infra.redis.domain.TokenForRedis;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
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
    public JwtPairDto createJwtPairAdmin(String userUUID, List roles) {
        JwtPairDto jwtPairDto = JwtPairDto.builder()
                .accessToken(jwtTokenizer.createAccessTokenAdmin(userUUID, roles))
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

        if (!tokenObject.getUserUUID().equals(userUUID)) {
            throw new BabpoolJwtException(JwtExceptionErrorCode.REFRESH_TOKEN_NOT_SAME_USER,
                    "user uuid in request refresh token and redis refresh token are not same, while extending login state.");
        }

        return jwtTokenizer.createAccessToken(userUUID, claims.get("roles", List.class));
    }

    @Override
    public void logout(String refreshTokenFromCookie) {
        Optional<TokenForRedis> tokenForRedisOptional = tokenRepository.findById(refreshTokenFromCookie);
        if (tokenForRedisOptional.isPresent()) {
            tokenRepository.deleteById(refreshTokenFromCookie);
        }else{
            log.error("refresh token not found in redis, while logout. token : {}", refreshTokenFromCookie);
        }
    }
}
