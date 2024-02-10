package com.swyp3.babpool.infra.health.application;

import com.swyp3.babpool.infra.health.dao.HealthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HealthCheckService {

    private final HealthRepository healthRepository;

    public Integer testConnection() {
        return healthRepository.countAll();
    }
}
