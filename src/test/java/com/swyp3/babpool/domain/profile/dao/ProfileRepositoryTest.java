package com.swyp3.babpool.domain.profile.dao;

import com.swyp3.babpool.domain.profile.application.response.ProfileKeywordsResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ActiveProfiles("test")
@MybatisTest
class ProfileRepositoryTest {

    @Autowired
    private ProfileRepository profileRepository;

    /**
     * H2 데이터베이스는 findKeywordsByUserId 매퍼에서 사용된 함수를 온전히 지원하지 않는다.
     * 따라서 조회된 세부 정보를 확인할 수 없어 Null 여부만 테스트 한다.
     */
    @DisplayName("findKeywords 매퍼는, 특정 사용자의 프로필 식별 값으로 키워드 목록을 조회한다.")
    @Test
    void findKeywords(){
          //given
            Long userId = 100000000000000001L;

            //when
            ProfileKeywordsResponse profileKeywordsResponse = profileRepository.findKeywordsByUserId(userId);

            //then
            log.info("profileKeywordsResponse : {}", profileKeywordsResponse);
            assertNotNull(profileKeywordsResponse);
    }
}