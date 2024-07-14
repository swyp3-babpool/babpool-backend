package com.swyp3.babpool.domain.keyword.dao;

import com.swyp3.babpool.domain.keyword.domain.Keyword;
import com.swyp3.babpool.domain.profile.application.response.ProfileKeywordsResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface KeywordRepository {

    Optional<ProfileKeywordsResponse> findKeywordsByUserId(Long userId);

    void saveKeyword(Keyword keyword);

    void saveUserAndKeywordMapping(Long mappingId, Long userId, Long keywordId);
}
