package com.swyp3.babpool.global.logging;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

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
    public CustomHttpLogMessage(Long responseTime, ContentCachingRequestWrapper requestWrapper, ContentCachingResponseWrapper responseWrapper, String clientIP) {
        this.httpMethod = requestWrapper.getMethod();
        this.requestURI = requestWrapper.getRequestURI();
        this.httpStatus = HttpStatus.valueOf(responseWrapper.getStatus());
        this.clientIP = clientIP;
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
        this.requestBody = truncateWhenStatusCodeNot200(new String(requestWrapper.getContentAsByteArray()), 1000);
        this.responseBody = truncateWhenStatusCodeNot200(new String(responseWrapper.getContentAsByteArray()), 1000);
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

    /**
     * 원본 문자열의 길이가 length 보다 길 경우, length 만큼 자르고 suffix 를 붙여 반환한다.
     * 단, 응답 코드가 4xx, 5xx 인 경우 원본 문자열을 그대로 반환한다.
     * @param str 원본 문자열
     * @param maxTruncatedLength 최대 문자열 길이
     * @return 최대 길이로 잘린 문자열
     */
    private String truncateWhenStatusCodeNot200(String str, int maxTruncatedLength) {
        if (this.httpStatus.is4xxClientError() || this.httpStatus.is5xxServerError()) {
            return str;
        }
        if (!str.isBlank() && str.length() > maxTruncatedLength) {
            return str.substring(0, maxTruncatedLength) + "... (truncated)";
        }
        return str;
    }


}