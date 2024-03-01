package com.swyp3.babpool.global.common.response.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApiResponseConfigProperties {

    private static String refreshTokenExpireDays;
    private static String domain;

    @Value("${property.jwt.refreshTokenExpireDays}")
    public void setRefreshTokenExpireDaysProperty(String fromProperty) {
        ApiResponseConfigProperties.refreshTokenExpireDays = fromProperty;
    }

    public static String getRefreshTokenExpireDays() {
        return refreshTokenExpireDays;
    }

    @Value("${property.cookie.domain}")
    public void setCookieDomainProperty(String fromProperty) {
        ApiResponseConfigProperties.domain = fromProperty;
    }

    public static String getDomain() {
        return domain;
    }

}
