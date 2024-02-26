package com.swyp3.babpool.infra.health.api;

import com.swyp3.babpool.infra.health.application.HealthCheckService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HealthCheckApi {

    private final HealthCheckService healthCheckService;

    @GetMapping("/api/test/connection")
    public ResponseEntity<Integer> testConnection() {
        return ResponseEntity.ok(healthCheckService.testConnection());
    }

    @GetMapping("/api/test/jwt/require")
    public ResponseEntity<String> testJwtRequire(@RequestHeader("userId") Long userId) {
        log.info("userId from request header : {}", userId);
        return ResponseEntity.ok("success");
    }

    @GetMapping("/api/test/jwt/permitted")
    public ResponseEntity<String> testJwtPermitted() {
        return ResponseEntity.ok("success");
    }
}
