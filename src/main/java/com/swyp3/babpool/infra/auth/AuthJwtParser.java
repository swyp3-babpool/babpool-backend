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
            throw new AuthException(AuthExceptionErrorCode.AUTH_UNSUPPORTED_ID_TOKEN_TYPE);
        }
    }

    public Claims parsePublicKeyAndGetClaims(String idToken, PublicKey publicKey) {
        try {
            return Jwts.parser()
                    .setSigningKey(publicKey)
                    .parseClaimsJws(idToken)
                    .getBody();
        }catch(ExpiredJwtException e){
            throw new AuthException(AuthExceptionErrorCode.AUTH_TOKEN_EXPIRED);
        }catch (UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e){
            throw new AuthException(AuthExceptionErrorCode.AUTH_MALFORMED_TOKEN);
        }
    }
}
