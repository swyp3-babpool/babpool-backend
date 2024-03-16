package com.swyp3.babpool.global.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

/**@apiNote JwtTokenizer is a class that provides a token for the user.
 * This class is used to create, validate, and (extract information) from the JWT token.
 */
@Slf4j
@Component
public class JwtTokenizer {

    private final byte[] accessSecret;
    private final byte[] refreshSecret;

    public final static Long ACCESS_TOKEN_EXPIRE_COUNT = 15 * 60 * 1000L; // 15 minutes
    public final static Long ACCESS_TOKEN_EXPIRE_COUNT_ADMIN = 1 * 24 * 60 * 60 * 1000L; // 1 days
    public final static Long REFRESH_TOKEN_EXPIRE_COUNT = 7 * 24 * 60 * 60 * 1000L; // 7 days

    public JwtTokenizer(@Value("${property.jwt.secretKey}") String accessSecret, @Value("${property.jwt.refreshKey}") String refreshSecret) {
        this.accessSecret = accessSecret.getBytes();
        this.refreshSecret = refreshSecret.getBytes();
    }


    public String createAccessToken(String uuid, List<String> roles) {
        return createToken(uuid, roles, ACCESS_TOKEN_EXPIRE_COUNT, accessSecret);
    }

    public String createAccessTokenAdmin(String uuid, List<String> roles) {
        return createToken(uuid, roles, ACCESS_TOKEN_EXPIRE_COUNT_ADMIN, accessSecret);
    }


    public String createRefreshToken(String uuid, List<String> roles) {
        return createToken(uuid, roles, REFRESH_TOKEN_EXPIRE_COUNT, refreshSecret);
    }


    private String createToken(String uuid, List<String> roles, Long expire, byte[] secretKey) {
        Claims claims = Jwts.claims().setSubject(uuid);
        claims.put("roles", roles);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + expire))
                .signWith(getSigningKey(secretKey))
                .compact();
    }

    public Claims parseAccessToken(String accessToken) {
        return parseToken(accessToken, accessSecret);
    }

    public Claims parseRefreshToken(String refreshToken) {
        return parseToken(refreshToken, refreshSecret);
    }

    public Claims parseToken(String token, byte[] secretKey) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey(secretKey))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**@apiNote getSigningKey is a method that returns a signing key from Secret.
     * This method is used to return a signing key for the user.
     * @param secretKey
     * @return Key
     */
    private Key getSigningKey(byte[] secretKey) {
        return Keys.hmacShaKeyFor(secretKey);
    }


}
