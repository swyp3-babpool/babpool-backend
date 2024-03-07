package com.swyp3.babpool.global.config;

import com.swyp3.babpool.global.jwt.JwtTokenInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcInterceptJwtConfig implements WebMvcConfigurer {

    private final JwtTokenInterceptor jwtTokenInterceptor;
    private static final String[] EXCLUDE_PATHS = {
        "/api/user/sign/in", "/api/user/sign/up", "/api/user/sign/out", "/api/token/access/refresh",
        "/api/test/connection", "/api/test/jwt/permitted", "/api/test/uuid", "/api/test/jwt/tokens", "/api/test/image/upload", "/api/test/image/delete", "/api/test/cookie", "/api/test/from/uuid/to/id", "/api/test/stomp/send",
        "/api/profile/list"
    };

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtTokenInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns(EXCLUDE_PATHS);
    }
}
