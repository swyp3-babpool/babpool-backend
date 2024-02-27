package com.swyp3.babpool.infra.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.security.PublicKey;
import io.jsonwebtoken.security.SignatureException;
import java.util.Base64;
import java.util.Map;

@Component
public class AuthJwtParser {
    private static final String IDENTITY_TOKEN_SPLITER = "\\.";
    private static final int HEADER_INDEX = 0;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public Map<String,String> parseHeaders(String identityToken){
        try {
            String encodedHeader = identityToken.split(IDENTITY_TOKEN_SPLITER)[HEADER_INDEX];
            String decodedHeader = new String(Base64.getDecoder().decode(encodedHeader));
            return objectMapper.readValue(decodedHeader, Map.class);
        } catch(JsonProcessingException | ArrayIndexOutOfBoundsException e) {
            //TODO: OAuthException 커스텀 에러 필요
            throw new IllegalStateException("JWTParser의 parseHeaders 오류");
        }
    }

    public Claims parsePublicKeyAndGetClaims(String idToken, PublicKey publicKey) {
        try {
            return Jwts.parser()
                    .setSigningKey(publicKey)
                    .parseClaimsJws(idToken)
                    .getBody();
        }catch(ExpiredJwtException e){
            //TODO: OAuthException 커스텀 에러 필요
            throw new IllegalStateException("JwtParser 중 오류: 만료된 JWT 토큰입니다.");
        }catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e){
            //TODO: OAuthException 커스텀 에러 필요
            throw new IllegalStateException("JwtParser 중 오류: 올바르지 않은 토큰입니다.");
        }
    }
}
