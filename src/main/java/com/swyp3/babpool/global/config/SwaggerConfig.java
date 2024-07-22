package com.swyp3.babpool.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private String TITLE = "Babpool API";
    private String DESCRIPTION = "Babpool API Specification";
    private String VERSION = "v1.0.0";
    private String SERVICE_URL = "https://bab-pool.com";
    private String BE_NAME = "HyunDo Song";
    private String BE_EMAIL = "guseh08@knou.ac.kr";
    private String BE_URL = "https://github.com/proHyundo";


    @Bean
    public OpenAPI customOpenAPI() {
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .scheme("Bearer")
                .name("Authorization");

        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearer-jwt");

        return new OpenAPI()
                .info(new Info()
                        .title(TITLE)
                        .description(DESCRIPTION)
                        .version(VERSION)
                        .termsOfService(SERVICE_URL)
                        .contact(new Contact().email(BE_EMAIL).name(BE_NAME).url(BE_URL)))
                .components(new Components().addSecuritySchemes("bearer-jwt", securityScheme)
                )
                .addSecurityItem(securityRequirement)
                ;
    }

    @Bean
    public GroupedOpenApi api() {
        String[] paths = {"/api/**"};
        String[] packagesToScan = {"com.swyp3.babpool.domain", "com.swyp3.babpool.global"};
        return GroupedOpenApi.builder().group("springdoc-openapi")
                .pathsToMatch(paths)
                .packagesToScan(packagesToScan)
                .build();
    }

    @Bean
    public GroupedOpenApi onlyForTestApi() {
        String[] paths = {"/api/test/jwt/tokens/{userId}"};
        String[] packagesToScan = {"com.swyp3.babpool.infra.health"};
        return GroupedOpenApi.builder().group("springdoc-openapi-only-for-test")
                .pathsToMatch(paths)
                .packagesToScan(packagesToScan)
                .build();
    }

}
