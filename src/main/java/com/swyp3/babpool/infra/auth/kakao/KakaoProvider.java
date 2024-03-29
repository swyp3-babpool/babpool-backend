package com.swyp3.babpool.infra.auth.kakao;

import com.swyp3.babpool.domain.user.exception.SignDownException;
import com.swyp3.babpool.domain.user.exception.errorcode.SignDownExceptionErrorCode;
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
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.security.PublicKey;
import java.util.Map;
import java.util.Optional;

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

    public void kakaoMemberSignOut(String oauthId) {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://kapi.kakao.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();

        MultiValueMap<String , String> params = new LinkedMultiValueMap<>();
        params.add("target_id_type","user_id");
        params.add("target_id",oauthId);

        Optional<String> responseJson = webClient.post()
                .uri(uriBuilder -> uriBuilder.path("/v1/user/logout").build())
                .header("Authorization", "KakaoAK " + KAKAO_SERVICE_APP_ADMIN_KEY)
                .body(BodyInserters.fromFormData(params))
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError() || status.is3xxRedirection(),
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .map(body -> new AuthException(AuthExceptionErrorCode.AUTH_SIGN_OUT_KAKAO_FAIL, body)))
                .bodyToMono(String.class)
                .doOnNext(response -> log.info("kakaoMemberSignOut response 응답 : " + response))
                .blockOptional();

        responseJson.ifPresentOrElse(
                json -> log.info("kakaoMemberSignOut Success: " + json),
                () -> log.error("kakaoMemberSignOut responseJson is empty")
        );

    }

    public void kakaoMemberDisconnect(String oauthId) {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://kapi.kakao.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();

        MultiValueMap<String , String> params = new LinkedMultiValueMap<>();
        params.add("target_id_type","user_id");
        params.add("target_id",oauthId);

        Optional<String> responseJson = webClient.post()
                .uri(uriBuilder -> uriBuilder.path("/v1/user/unlink").build())
                .header("Authorization", "KakaoAK " + KAKAO_SERVICE_APP_ADMIN_KEY)
                .body(BodyInserters.fromFormData(params))
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError() || status.is3xxRedirection(),
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .map(body -> new AuthException(AuthExceptionErrorCode.AUTH_DISCONNECT_KAKAO_FAIL,body)))
                .bodyToMono(String.class)
                .doOnNext(response -> log.info("kakaoMemberSignOut response 응답 : " + response))
                .blockOptional();

        responseJson.ifPresentOrElse(
                json -> log.info("kakaoMemberDisconnect Success: " + json),
                () -> log.error("kakaoMemberDisconnect responseJson is empty")
        );

    }

}
