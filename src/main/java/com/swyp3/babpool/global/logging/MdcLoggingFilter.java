package com.swyp3.babpool.global.logging;

import com.fasterxml.uuid.Generators;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.util.*;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class MdcLoggingFilter extends OncePerRequestFilter{

    private static ArrayList<String> NOT_LOGGING_MONITORING = new ArrayList<>(Arrays.asList(
            "/api/actuator/**"
    ));

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        MDC.put("request_uuid", UUID.randomUUID().toString().substring(0, 8));

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        filterChain.doFilter(requestWrapper, responseWrapper);
        stopWatch.stop();

        CustomHttpLogMessage.builder()
                .requestWrapper(requestWrapper)
                .responseWrapper(responseWrapper)
                .responseTime(stopWatch.getTotalTimeMillis())
                .build()
                .toPrettierLog();

        responseWrapper.copyBodyToResponse();

        MDC.remove("request_uuid");
        MDC.clear();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        if (NOT_LOGGING_MONITORING.contains(request.getRequestURI())) {
            log.info("Monitoring URI: {}", request.getRequestURI(), "\t Client IP: {}", request.getRemoteAddr());
            return true;
        }
        return false;
    }
}
