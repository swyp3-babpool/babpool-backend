package com.swyp3.babpool.domain.appointment.config;

import com.swyp3.babpool.global.jwt.JwtAuthenticator;
import com.swyp3.babpool.global.jwt.exception.BadCredentialsException;
import com.swyp3.babpool.global.jwt.exception.errorcode.JwtExceptionErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.security.WeakKeyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * WebSocketHandler for validating jwt access token in Stomp Header
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AppointmentWebSocketHandler implements ChannelInterceptor {

    private final JwtAuthenticator jwtAuthenticator;
    public static final String AUTHORIZATION = "Authorization";
    public static final String BEARER = "Bearer";

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if (!accessor.getCommand().equals(StompCommand.CONNECT)){
            return message;
        }
        String authorization = accessor.getFirstNativeHeader(AUTHORIZATION);
        String accessToken = getAccessTokenFrom(authorization);
        log.info("accessToken in AppointmentWebSocketHandler.class: {}", accessToken);

        Claims authenticatedClaims = null;
        try {
            if (StringUtils.hasText(accessToken)) {
                authenticatedClaims = jwtAuthenticator.authenticate(accessToken);
            }
            if (!authenticatedClaims.isEmpty()){
                Long userId = jwtAuthenticator.jwtTokenUserIdResolver(authenticatedClaims.getSubject());
//                accessor.getSessionAttributes().put("userId", userId);
//                accessor.setHeader("userId", userId);
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
            e.printStackTrace();
            log.error("}");
            log.error("====================================================");
            throw new BadCredentialsException(JwtExceptionErrorCode.NOT_FOUND_TOKEN, "throw new exception");
        }
        return message;
    }

    private String getAccessTokenFrom(String authorization) {
        if (StringUtils.hasText(authorization) && authorization.startsWith(BEARER)) {
            return authorization.split(" ")[1];
        }
        return null;
    }
}
