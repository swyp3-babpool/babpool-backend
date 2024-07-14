package com.swyp3.babpool.domain.keyword.application;

import com.swyp3.babpool.domain.keyword.dao.KeywordRepository;
import com.swyp3.babpool.domain.keyword.domain.MappingUserKeyword;
import com.swyp3.babpool.domain.keyword.exception.KeywordErrorCode;
import com.swyp3.babpool.domain.keyword.exception.KeywordException;
import com.swyp3.babpool.domain.profile.application.response.ProfileKeywordsResponse;
import com.swyp3.babpool.global.tsid.TsidKeyGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class KeywordServiceImpl implements KeywordService{

    private final KeywordRepository keywordRepository;
    private final TsidKeyGenerator tsidKeyGenerator;

    @Transactional
    @Override
    public void saveUserAndKeywordMapping(Long userId, List<Long> keywordList) {
        List<MappingUserKeyword> mappingList = keywordList.stream()
                        .map(keywordId -> new MappingUserKeyword(tsidKeyGenerator.generateTsid(), userId, keywordId))
                        .toList();
        keywordRepository.saveUserAndKeywordMappingForEach(mappingList);
    }

    @Override
    public ProfileKeywordsResponse getKeywordsAndSubjectsByUserId(Long userId) {
        return keywordRepository.findKeywordsByUserId(userId).orElseThrow(
                () -> new KeywordException(KeywordErrorCode.KEYWORD_NOT_FOUND, "해당 사용자의 키워드 정보가 존재하지 않습니다.")
        );
    }

    @Transactional
    @Override
    public void deleteAllKeywordsOf(Long userId) {
        keywordRepository.deleteAllKeywordMappingByUserId(userId);
    }
}
