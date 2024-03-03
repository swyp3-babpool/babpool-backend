package com.swyp3.babpool.global.jwt.application;

import com.swyp3.babpool.global.jwt.application.response.JwtPairDto;

import java.util.List;

public interface JwtService {

    JwtPairDto createJwtPair(String userUUID, List roles);

    String extendLoginState(String refreshToken);

    void logout(String refreshToken);
}
