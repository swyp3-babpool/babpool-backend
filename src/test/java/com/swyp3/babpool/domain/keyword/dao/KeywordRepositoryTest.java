package com.swyp3.babpool.domain.keyword.dao;

import com.swyp3.babpool.domain.keyword.dao.response.KeywordAndUserResponseDto;
import com.swyp3.babpool.domain.keyword.domain.MappingUserKeyword;
import com.swyp3.babpool.domain.profile.application.response.ProfileKeywordsResponse;
import com.swyp3.babpool.global.tsid.TsidKeyGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ActiveProfiles("test")
@MybatisTest
class KeywordRepositoryTest {

    @Autowired
    private KeywordRepository keywordRepository;
    private static TsidKeyGenerator tsidKeyGenerator;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeAll
    static void setUp(){
        tsidKeyGenerator = new TsidKeyGenerator();
    }


    /**
     * H2 데이터베이스는 findKeywordsByUserId 매퍼에서 사용된 함수를 온전히 지원하지 않는다.
     * 따라서 조회된 세부 정보를 확인할 수 없어 Null 여부만 테스트 한다.
     */
    @DisplayName("findKeywords 매퍼는, 특정 사용자의 프로필 식별 값으로 키워드 목록을 조회한다.")
    @Test
    void findKeywords(){
        //given
        Long userId = 100000000000000001L;
        String query = "SELECT JSON_OBJECTAGG(subject_name : keyword_name_list) AS keywords FROM (" +
                " SELECT tk.keyword_subject AS subject_name, JSON_ARRAYAGG(tk.keyword_name) AS keyword_name_list" +
                " FROM t_keyword tk INNER JOIN t_m_user_keyword tmuk ON tk.keyword_id = tmuk.keyword_id" +
                " WHERE tmuk.user_id = "+ userId +" GROUP BY tk.keyword_subject)tb_json_arrayagg";
        //when
        String keywordsResponse = jdbcTemplate.queryForObject(query, (rs, rowNum) -> rs.getString("keywords"));

        //then
        log.info("keywordsResponse : {}", keywordsResponse);
        assertNotNull(keywordsResponse);
    }

    @DisplayName("findAllByUserId 매퍼는, 특정 사용자의 식별 값으로 KeywordAndUserResponseDto 목록을 조회한다.")
    @Test
    void findAllByUserId(){
        //given
        Long userId = 100000000000000001L;

        //when
        List<KeywordAndUserResponseDto> keywordsResponse = keywordRepository.findAllByUserId(userId);

        //then
        assertThat(keywordsResponse)
                .hasSize(3)
                .extracting("keywordId")
                .containsExactly(400000000000000001L, 400000000000000002L, 400000000000000003L);
    }

    @DisplayName("saveUserAndKeywordMappingForEach 매퍼는, t_m_user_keyword 테이블에 매핑 식별 값, 사용자 식별 값, 키워드 식별 값 리스트를 저장한다.")
    @Test
    void saveUserAndKeywordMappingForEach(){
        //given
        Long userId = 100000000000000005L;
        List<Long> keywordIds = List.of(400000000000000002L, 400000000000000003L);

        List<MappingUserKeyword> mappingList = keywordIds.stream()
                .map(keywordId -> MappingUserKeyword.builder()
                        .mappingId(tsidKeyGenerator.generateTsid())
                        .userId(userId)
                        .keywordId(keywordId)
                        .build())
                .toList();

        //when
        keywordRepository.saveUserAndKeywordMappingForEach(mappingList);

        //then
        List<KeywordAndUserResponseDto> keywordsResponse = keywordRepository.findAllByUserId(userId);
        keywordsResponse.forEach(dto -> log.info(dto.toString()));
        assertThat(keywordsResponse.size()).isEqualTo(3);
    }

    @DisplayName("deleteAllKeywordMappingByUserId 매퍼는, 특정 사용자의 식별 값으로 t_m_user_keyword 테이블의 모든 매핑 정보를 삭제한다.")
    @Test
    void deleteAllKeywordMappingByUserId(){
        //given
        Long userId = 100000000000000001L;

        //when
        keywordRepository.deleteAllKeywordMappingByUserId(userId);

        //then
        List<KeywordAndUserResponseDto> keywordsResponse = keywordRepository.findAllByUserId(userId);
        assertThat(keywordsResponse).isEmpty();
    }
}