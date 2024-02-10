package com.swyp3.babpool.infra.health.api;

import com.swyp3.babpool.infra.health.application.HealthCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HealthCheckApi {

    private final HealthCheckService healthCheckService;

    @GetMapping("/api/test/connection")
    public ResponseEntity<Integer> testConnection() {
        return ResponseEntity.ok(healthCheckService.testConnection());
    }
}
