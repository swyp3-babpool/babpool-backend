package com.swyp3.babpool.global.config;

import com.swyp3.babpool.global.config.swagger.SwaggerAccessInterceptor;
import com.swyp3.babpool.global.jwt.JwtTokenInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcInterceptJwtConfig implements WebMvcConfigurer {

    private final JwtTokenInterceptor jwtTokenInterceptor;
    private final SwaggerAccessInterceptor swaggerAccessInterceptor;
    private static final String[] EXCLUDE_PATHS = {
        "/api/user/sign/in", "/api/user/sign/up", "/api/user/sign/out", "/api/token/access/refresh",
        "/api/profile/list"
    };

    private static final String[] MONITORING = {
        "/api/actuator/**"
    };

    private static final String[] TESTING = {
        "/api/test/**"
    };

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtTokenInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns(EXCLUDE_PATHS)
                .excludePathPatterns(MONITORING)
                .excludePathPatterns(TESTING);
        registry.addInterceptor(swaggerAccessInterceptor)
                .addPathPatterns("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**", "/docs/**");
    }
}
