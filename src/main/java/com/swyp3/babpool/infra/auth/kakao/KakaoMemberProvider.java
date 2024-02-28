package com.swyp3.babpool.infra.auth.kakao;

import com.swyp3.babpool.infra.auth.AuthJwtParser;
import com.swyp3.babpool.infra.auth.PublicKeyGenerator;
import com.swyp3.babpool.infra.auth.exception.AuthException;
import com.swyp3.babpool.infra.auth.exception.errorcode.AuthExceptionErrorCode;
import com.swyp3.babpool.infra.auth.response.AuthMemberResponse;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.PublicKey;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class KakaoMemberProvider {
    private final AuthJwtParser authJwtParser;
    private final PublicKeyGenerator publicKeyGenerator;
    private final KakaoClient kakaoClient;
    @Value("${property.oauth.kakao.iss}")
    private String iss;
    @Value("${property.oauth.kakao.client-id}")
    private String clientId;

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
            throw new AuthException(AuthExceptionErrorCode.AUTH_JWT_ERROR);
        }
    }

}
