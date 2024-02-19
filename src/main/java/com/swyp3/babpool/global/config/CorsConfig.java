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

    private String[] clientUrl;
    public CorsConfig( @Value("${property.url.clientUrl}") String[] clientUrl) {
        this.clientUrl = clientUrl;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        log.info("current clientUrl: {}", Arrays.toString(clientUrl));
        registry
            .addMapping("/**")
            .allowedOrigins(clientUrl)
            .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true);
    }
}
