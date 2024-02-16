package com.swyp3.babpool.infra.health.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@MybatisTest
class HealthRepositoryTest {

    @Autowired
    HealthRepository healthRepository;

    @DisplayName("DB 초기화 후, 전체 행 개수를 조회하면 3이어야 한다.")
    @Test
    void countAllResult3AfterInit() {
        // when
        Integer resultInt = healthRepository.countAll();
        // then
        assertThat(resultInt).isEqualTo(3);
    }

}