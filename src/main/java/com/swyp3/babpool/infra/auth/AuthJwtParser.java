package com.swyp3.babpool.infra.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swyp3.babpool.infra.auth.exception.AuthException;
import com.swyp3.babpool.infra.auth.exception.errorcode.AuthExceptionErrorCode;
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
            throw new AuthException(AuthExceptionErrorCode.AUTH_UNSUPPORTED_ID_TOKEN_TYPE,
                    "Identity Token 헤더 parse 중 문제가 발생했습니다.");
        }
    }

    public Claims parsePublicKeyAndGetClaims(String idToken, PublicKey publicKey) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(idToken)
                    .getBody();
        }catch(ExpiredJwtException e){
            throw new AuthException(AuthExceptionErrorCode.AUTH_TOKEN_EXPIRED,
                    "유효기간이 만료된 Identity Token 입니다.");
        }catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e){
            throw new AuthException(AuthExceptionErrorCode.AUTH_MALFORMED_TOKEN,
                    "유효하지 않은 Identity Token 입니다.");
        }
    }
}
