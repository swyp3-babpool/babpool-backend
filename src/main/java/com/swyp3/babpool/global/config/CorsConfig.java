package com.swyp3.babpool.global.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Slf4j
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Value("${property.url.clientUrl}")
    private String clientUrl;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        log.info("current clientUrl: {}, {}, {}", clientUrl);
        registry
            .addMapping("/**")
            .allowedOrigins(clientUrl) // String... origins
            .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true);
    }
}
