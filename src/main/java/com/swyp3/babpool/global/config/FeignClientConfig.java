package com.swyp3.babpool.global.config;

import com.swyp3.babpool.BabpoolApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackageClasses = BabpoolApplication.class)
public class FeignClientConfig {
}
