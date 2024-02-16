package com.swyp3.babpool.global.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    // TODO : @Value 어노테이션, Caused by: java.lang.IllegalArgumentException: Could not resolve placeholder 'property.url.clientUrl' in value "${property.url.clientUrl}" 에러 발생
//    @Value("${property.url.clientUrl}")
//    private String[] clientUrl;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
            .addMapping("/**")
//            .allowedOrigins(clientUrl)
            .allowedOrigins("http://localhost:5173", "https://bab-pool.com", "https://www.bab-pool.com")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true);
    }
}
