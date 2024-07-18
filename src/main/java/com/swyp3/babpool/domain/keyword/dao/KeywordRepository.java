package com.swyp3.babpool.domain.keyword.dao;

import com.swyp3.babpool.domain.keyword.dao.response.KeywordAndUserResponseDto;
import com.swyp3.babpool.domain.keyword.domain.Keyword;
import com.swyp3.babpool.domain.keyword.domain.MappingUserKeyword;
import com.swyp3.babpool.domain.profile.application.response.ProfileKeywordsResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface KeywordRepository {

    // 테스트 코드 작성 완료
    Optional<ProfileKeywordsResponse> findKeywordsByUserId(Long userId);

    // 테스트 코드 작성 완료
    List<KeywordAndUserResponseDto> findAllByUserId(Long userId);

    // 테스트 코드 작성 완료
    void saveUserAndKeywordMappingForEach(@Param("mappingList") List<MappingUserKeyword> mappingList);

    // 테스트 코드 작성 완료
    void deleteAllKeywordMappingByUserId(Long userId);
}
