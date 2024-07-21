package com.swyp3.babpool.global.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.*;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MdcLoggingFilter extends OncePerRequestFilter{

    private static final AntPathMatcher pathMatcher = new AntPathMatcher();
    private static ArrayList<String> LOGGING_EXCLUDE_URLS = new ArrayList<>(Arrays.asList(
            "/api/actuator/**"
    ));
    private static ArrayList<String> MONITORING_ALLOWED_IPS = new ArrayList<>(Arrays.asList(
            "34.168.207.216"
    ));

    @Value("${property.header.x-babpool-local-front}")
    private String X_BABPOOL_LOCAL_FRONT_VALUE;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        MDC.put("request_uuid", UUID.randomUUID().toString().substring(0, 8));

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        setOriginAttributeAtRequest(requestWrapper);
        filterChain.doFilter(requestWrapper, responseWrapper);
        stopWatch.stop();


        CustomHttpLogMessage.builder()
                .requestWrapper(requestWrapper)
                .responseWrapper(responseWrapper)
                .responseTime(stopWatch.getTotalTimeMillis())
                .clientIP(ClientIPResolver.getClientIP(requestWrapper))
                .build()
                .toPrettierLog();

        responseWrapper.copyBodyToResponse();

        MDC.remove("request_uuid");
        MDC.clear();
    }

    /**
     * 클라이언트 Origin 이 LOCAL_HOST_5173 인 경우 request 에 localhostFlag 속성을 추가한다.
     * @param requestWrapper
     */
    void setOriginAttributeAtRequest(ContentCachingRequestWrapper requestWrapper) {
        String requestOrigin = requestWrapper.getHeader("x-babpool-local-front");
        if (StringUtils.hasText(requestOrigin) && requestOrigin.equals(X_BABPOOL_LOCAL_FRONT_VALUE)) {
            requestWrapper.setAttribute("localhostFlag", "true");
        }
    }

    /**
     * 모니터링 요청인 경우 MDC 로깅에서 제외한다.
     * 단, 허용되지 않은 IP에서 전송된 모니터링 요청은 error 레벨로 로깅된다.
     * @return boolean : MDC 로깅에서 제외할지 여부
     * @throws ServletException
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);

        boolean urlMatchResult = LOGGING_EXCLUDE_URLS.stream().anyMatch(
                uri -> pathMatcher.match(uri, requestWrapper.getRequestURI())
        );

        if (urlMatchResult) {
            String clientIP = ClientIPResolver.getClientIP(requestWrapper);
            if (!MONITORING_ALLOWED_IPS.contains(clientIP)) {
                log.error("Monitoring Request From Invalid IP.\t URI: {} \t Client IP: {}", requestWrapper.getRequestURI(), clientIP);
            }
            return true;
        }
        return false;
    }
}
