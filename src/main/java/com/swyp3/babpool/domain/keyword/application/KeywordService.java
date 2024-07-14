package com.swyp3.babpool.domain.keyword.application;

import com.swyp3.babpool.domain.profile.application.response.ProfileKeywordsResponse;

public interface KeywordService {
    void saveUserAndKeywordMapping(Long userId, Long keywordId);

    ProfileKeywordsResponse getKeywordsAndSubjectsByUserId(Long userId);
}
