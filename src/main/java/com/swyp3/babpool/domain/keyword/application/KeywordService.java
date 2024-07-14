package com.swyp3.babpool.domain.keyword.application;

import com.swyp3.babpool.domain.profile.application.response.ProfileKeywordsResponse;

import java.util.List;

public interface KeywordService {
    void saveUserAndKeywordMapping(Long userId, List<Long> keywordList);

    ProfileKeywordsResponse getKeywordsAndSubjectsByUserId(Long userId);

    void deleteAllKeywordsOf(Long userId);
}
