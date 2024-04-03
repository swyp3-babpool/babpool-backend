package com.swyp3.babpool.global.logging;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@Slf4j
@Getter
public class CustomHttpLogMessage {
    String httpMethod;
    String requestURI;
    HttpStatus httpStatus;
    String clientIP;
    Long responseTime;
    String headers;
    String requestParam;
    String requestBody;
    String responseBody;

    @Builder
    public CustomHttpLogMessage(Long responseTime, ContentCachingRequestWrapper requestWrapper, ContentCachingResponseWrapper responseWrapper) {
        this.httpMethod = requestWrapper.getMethod();
        this.requestURI = requestWrapper.getRequestURI();
        this.httpStatus = HttpStatus.valueOf(responseWrapper.getStatus());
        this.clientIP = requestWrapper.getRemoteAddr();
        this.responseTime = responseTime;
        Map<String, String> requestHeaderMap = new HashMap<>();
        Enumeration headerNames = requestWrapper.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = requestWrapper.getHeader(key);
            requestHeaderMap.put(key, value);
        }
        this.headers = requestHeaderMap.toString();
        StringBuilder requestParamBuilder = new StringBuilder();
        requestWrapper.getParameterMap().entrySet().stream().forEach(entry ->{
                requestParamBuilder.append(entry.getKey() + "=" + entry.getValue()[0]);
        });
        this.requestParam = requestParamBuilder.toString();
        this.requestBody = new String(requestWrapper.getContentAsByteArray());
        this.responseBody = new String(responseWrapper.getContentAsByteArray());
    }

    public void toPrettierLog() {
        log.info("\n" +
                "┌============= HTTP LOG MESSAGE =============\n" +
                "| HTTP REQUEST : {} {}\n" +
                "| HTTP STATUS: {}\n" +
                "| CLIENT IP: {}\n" +
                "| RESPONSE TIME: {}ms\n" +
                "| HEADERS: {}\n" +
                "| REQUEST PARAM: {}\n" +
                "| REQUEST BODY: {}\n" +
                "| RESPONSE BODY: {}\n" +
                "└============================================",
                httpMethod, requestURI, httpStatus, clientIP, responseTime, headers, requestParam, requestBody, responseBody);
    }

}