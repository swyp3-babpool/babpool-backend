package com.swyp3.babpool.domain.user.dao;

import com.swyp3.babpool.global.tsid.TsidKeyGenerator;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ActiveProfiles("test")
@MybatisTest
class ExitInfoRepositoryTest {

    @Autowired
    private ExitInfoRepository exitInfoRepository;

    private static TsidKeyGenerator tsidKeyGenerator;

    @BeforeAll
    static void beforeAll() {
        tsidKeyGenerator = new TsidKeyGenerator();
    }

    @DisplayName("saveExitInfo 매퍼는 exitId, userId, exitReason을 받아 저장한다.")
    @Test
    void saveExitInfo() {
        // given
        Long exitId = tsidKeyGenerator.generateTsid();
        Long userId = 100000000000000005L;
        String exitReason = "테스트 사용자 탈퇴";

        // when
        exitInfoRepository.saveExitInfo(exitId, userId, exitReason);

        // then
        Map<String, Object> result = exitInfoRepository.findById(exitId);
        Assertions.assertThat(result.get("EXIT_ID").toString()).isEqualTo(exitId.toString());
    }

    @DisplayName("findById 매퍼는 exitId를 받아 해당하는 탈퇴 정보를 반환한다.")
    @Test
    void findById() {
        // given
        Long exitId = tsidKeyGenerator.generateTsid();
        Long userId = 100000000000000005L;
        String exitReason = "테스트 사용자 탈퇴";
        exitInfoRepository.saveExitInfo(exitId, userId, exitReason);

        // when
        Map<String, Object> result = exitInfoRepository.findById(exitId);

        // then
        Assertions.assertThat(result.get("EXIT_ID").toString()).isEqualTo(exitId.toString());
    }
}