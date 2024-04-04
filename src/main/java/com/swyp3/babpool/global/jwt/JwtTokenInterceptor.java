package com.swyp3.babpool.global.jwt;

import com.swyp3.babpool.global.jwt.exception.BadCredentialsException;
import com.swyp3.babpool.global.jwt.exception.errorcode.JwtExceptionErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.IOException;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.security.WeakKeyException;
import io.netty.util.internal.StringUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.servlet.HandlerInterceptor;


@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenInterceptor implements HandlerInterceptor {

    private final JwtAuthenticator jwtAuthenticator;
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException, IOException {
        if(CorsUtils.isPreFlightRequest(request)){
            log.debug("if request is preflight, return true");
            return true;
        }

        String accessToken = StringUtil.EMPTY_STRING;
        Claims authenticatedClaims = null;
        try {
            accessToken = getAccessTokenFrom(request);
            if (StringUtils.hasText(accessToken)) {
                authenticatedClaims = jwtAuthenticator.authenticate(accessToken);
            }
            if (!authenticatedClaims.isEmpty()){
                Long userId = jwtAuthenticator.jwtTokenUserIdResolver(authenticatedClaims.getSubject());
                request.setAttribute("userId", userId);
            }
        } catch (NullPointerException | IllegalStateException e) {
            log.error("Not found Token // token : {}", accessToken);
            throw new BadCredentialsException(JwtExceptionErrorCode.NOT_FOUND_TOKEN, "throw new not found token exception");
        } catch (SecurityException | MalformedJwtException | SignatureException | WeakKeyException e) {
            log.error("Invalid Token // token : {}", accessToken);
            throw new BadCredentialsException(JwtExceptionErrorCode.INVALID_TOKEN, "throw new invalid token exception");
        } catch (ExpiredJwtException e) {
            log.error("EXPIRED Token // token : {}", accessToken);
            throw new BadCredentialsException(JwtExceptionErrorCode.EXPIRED_TOKEN, "throw new expired token exception");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported Token // token : {}", accessToken);
            throw new BadCredentialsException(JwtExceptionErrorCode.UNSUPPORTED_TOKEN, "throw new unsupported token exception");
        } catch (Exception e) {
            log.error("====================================================");
            log.error("Babpool JwtTokenInterceptor - preHandle() 오류 발생");
            log.error("token : {}", accessToken);
            log.error("Exception Message : {}", e.getMessage());
            log.error("Exception StackTrace : {");
            e.getStackTrace()[0].toString();
            log.error("}");
            log.error("====================================================");
            throw new BadCredentialsException(JwtExceptionErrorCode.NOT_FOUND_TOKEN, "throw new exception");
        }
        return true;
    }


    private String getAccessTokenFrom(HttpServletRequest request) {
        String authorization = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(authorization) && authorization.startsWith(BEARER)) {
            return authorization.split(" ")[1];
        }
        return null;
    }
}
