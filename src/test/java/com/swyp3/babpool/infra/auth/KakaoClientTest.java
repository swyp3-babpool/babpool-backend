package com.swyp3.babpool.infra.auth;

import com.swyp3.babpool.global.config.FeignClientConfig;
import com.swyp3.babpool.infra.auth.BabpoolPublicKey;
import com.swyp3.babpool.infra.auth.kakao.KakaoClient;
import com.swyp3.babpool.infra.auth.kakao.KakaoPublicKeys;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.net.http.HttpClient;
import java.util.List;
import java.util.Objects;

@Slf4j
@SpringBootTest
public class KakaoClientTest {
    @Autowired
    private KakaoClient kakaoClient;

    @Test
    @DisplayName("KAKAO 서버와 통신하여 Kakao public keys 응답을 받는다")
    void getPublicKeys(){
        KakaoPublicKeys kakaoPublicKeys = kakaoClient.getKakaoOIDCOpenKeys();
        List<BabpoolPublicKey> keys = kakaoPublicKeys.getKeys();

        boolean isRequestedKeysNonNull = keys.stream()
                .allMatch(this::isAllNotNull);
        Assertions.assertThat(isRequestedKeysNonNull).isTrue();

        log.info("kid1: {}", keys.get(0).getKid());
        log.info("alg1: {}", keys.get(0).getAlg());
        log.info("kid2: {}", keys.get(1).getKid());
        log.info("alg2: {}", keys.get(1).getAlg());
    }

    private boolean isAllNotNull(BabpoolPublicKey babpoolPublicKey) {
        return Objects.nonNull(babpoolPublicKey.getKty()) && Objects.nonNull(babpoolPublicKey.getKid()) &&
                Objects.nonNull(babpoolPublicKey.getUse()) && Objects.nonNull(babpoolPublicKey.getAlg()) &&
                Objects.nonNull(babpoolPublicKey.getN()) && Objects.nonNull(babpoolPublicKey.getE());
    }
}
