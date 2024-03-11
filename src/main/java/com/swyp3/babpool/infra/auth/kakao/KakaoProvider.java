package com.swyp3.babpool.infra.auth.kakao;

import com.swyp3.babpool.infra.auth.AuthJwtParser;
import com.swyp3.babpool.infra.auth.PublicKeyGenerator;
import com.swyp3.babpool.infra.auth.exception.AuthException;
import com.swyp3.babpool.infra.auth.exception.errorcode.AuthExceptionErrorCode;
import com.swyp3.babpool.infra.auth.response.AuthMemberResponse;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.security.PublicKey;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoProvider {
    private final AuthJwtParser authJwtParser;
    private final PublicKeyGenerator publicKeyGenerator;
    private final KakaoClient kakaoClient;
    @Value("${property.oauth.kakao.iss}")
    private String iss;
    @Value("${property.oauth.kakao.client-id}")
    private String clientId;
    @Value("${property.oauth.kakao.admin-key}")
    private String KAKAO_SERVICE_APP_ADMIN_KEY;

    public AuthMemberResponse getKakaoPlatformMember(String identityToken){
        Map<String, String> headers = authJwtParser.parseHeaders(identityToken);
        KakaoPublicKeys kakaoPublicKeys = kakaoClient.getKakaoOIDCOpenKeys();
        PublicKey publicKey = publicKeyGenerator.generateKakaoPublicKey(headers, kakaoPublicKeys);

        Claims claims = authJwtParser.parsePublicKeyAndGetClaims(identityToken, publicKey);
        validateClaims(claims);

        return new AuthMemberResponse(claims.getSubject(),claims.get("nickname",String.class),claims.get("picture",String.class),claims.get("email",String.class));
    }

    private void validateClaims(Claims claims) {
        if(!claims.getIssuer().contains(iss)&&claims.getAudience().equals(clientId)){
            throw new AuthException(AuthExceptionErrorCode.AUTH_JWT_ERROR,
                    "OAuth Claim 값이 올바르지 않습니다.");
        }
    }

    public void kakaoMemberSignOut(Long userId) {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://kauth.kakao.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        JSONObject response = webClient.post()
                .uri(uriBuilder -> uriBuilder.path("/v1/user/logout").build())
                .header("Authorization", "KakaoAK " + clientId)
                .retrieve().bodyToMono(JSONObject.class).block();

        log.info("kakaoMemberSignOut response: " + response.toString());
    }

    public void kakaoMemberDisconnect(Long oauthId) {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://kapi.kakao.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        JSONObject response = webClient.post()
                .uri(uriBuilder -> uriBuilder.path("/v1/user/unlink").build())
                .header("Authorization", "KakaoAK " + KAKAO_SERVICE_APP_ADMIN_KEY)
                .body(Mono.just(
                        Map.of("target_id_type", "user_id",
                                "target_id", oauthId)), JSONObject.class)
                .retrieve().bodyToMono(JSONObject.class).block();

        log.info("kakaoMemberDisconnect response: " + response.toString());

    }

}
