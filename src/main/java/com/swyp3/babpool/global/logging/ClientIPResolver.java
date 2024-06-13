package com.swyp3.babpool.global.logging;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

public class ClientIPResolver {

    /**
     * "x-forwarded-for", "x-real-ip" 헤더를 통해 클라이언트 IP를 반환한다.
     * IP를 찾을 수 없는 경우 "unknown" 문자열을 반환한다.
     * @param httpServletRequest
     * @return 클라이언트 IP
     */
    public static String getClientIP(HttpServletRequest httpServletRequest) {
        String clientIP = null;

        String xff = httpServletRequest.getHeader("X-Forwarded-For");
        if (StringUtils.hasText(xff)){
            List<String> xffList = Arrays.asList(xff.split(","));
            clientIP = xffList.get(0).trim();
        }

        if (!StringUtils.hasText(clientIP)) {
            clientIP = httpServletRequest.getHeader("x-real-ip");
        }

        if (!StringUtils.hasText(clientIP)) {
            clientIP = "unknown";
        }

        return clientIP;
    }

}
