package com.swyp3.babpool.infra.auth.kakao;

import com.swyp3.babpool.infra.auth.BabpoolPublicKey;
import com.swyp3.babpool.infra.auth.domain.Auth;
import com.swyp3.babpool.infra.auth.exception.AuthException;
import com.swyp3.babpool.infra.auth.exception.errorcode.AuthExceptionErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class KakaoPublicKeys {
    private List<BabpoolPublicKey> keys;

    public BabpoolPublicKey getMatchesKey(String kid) {
        return this.keys.stream()
                .filter(key -> key.getKid().equals(kid))
                .findFirst()
                .orElseThrow(() -> new AuthException(AuthExceptionErrorCode.AUTH_JWT_ERROR));
    }
}
