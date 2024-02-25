package com.swyp3.babpool.global.util.jwt.application;

import com.swyp3.babpool.global.util.jwt.application.response.JwtPairDto;

import java.util.List;

public interface JwtService {

    JwtPairDto createJwtPair(String userUUID, List roles);

    String extendLoginState(String refreshToken);

    void logout(String refreshToken);
}
