package com.swyp3.babpool.infra.auth.kakao;

import com.swyp3.babpool.infra.auth.BabpoolPublicKey;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class KakaoPublicKeys {
    private List<BabpoolPublicKey> keys;

    public BabpoolPublicKey getMatchesKey(String kid) {
        //TODO: OAuthException 커스텀 에러 필요
        return this.keys.stream()
                .filter(key -> key.getKid().equals(kid))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Kakao JWT 값의 kid 정보가 올바르지 않습니다."));
    }
}
