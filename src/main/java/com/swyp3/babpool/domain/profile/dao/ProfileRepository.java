package com.swyp3.babpool.domain.profile.dao;

import com.swyp3.babpool.domain.profile.api.request.ProfilePagingConditions;
import com.swyp3.babpool.domain.profile.application.response.ProfilePagingResponse;
import com.swyp3.babpool.domain.profile.domain.Profile;
import com.swyp3.babpool.global.common.request.PagingRequestList;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper
public interface ProfileRepository {

    void saveProfileImageUrl(Profile profile);

    /**
     * 프로필 리스트 조회
     * @param pagingRequestList
     *   - search : 검색어 (한 줄 소개, 자기소개 본문)
     *   - userGrades : 사용자 구분, [FIRST_GRADE, SECOND_GRADE, THIRD_GRADE, FOURTH_GRADE, GRADUATE, POST_GRADUATE]
     *   - keywords : 키워드 식별 값 리스트
     *   - pageable : 페이징 정보
     *     - page : 페이지 번호 (0부터 시작)
     *     - size : 페이지 사이즈 (기본값 10)
     *     - sort : 정렬 정보, ex) sort=createdAt,desc
     * @return 프로필 응답 리스트
     */
    List<ProfilePagingResponse> findAllByPageable(PagingRequestList<?> pagingRequestList);

    /**
     * 페이징 처리를 위한 카운트 조회
     * @param profilePagingConditions
     *   - search : 검색어 (한 줄 소개, 자기소개 본문)
     *   - userGrades : 사용자 구분, [FIRST_GRADE, SECOND_GRADE, THIRD_GRADE, FOURTH_GRADE, GRADUATE, POST_GRADUATE]
     *   - keywords : 키워드 식별 값 리스트
     * @return 조건 검색 결과 개수
     */
    int countByPageable(ProfilePagingConditions profilePagingConditions);

    Profile findById(Long profileId);

    ProfileDetailDaoDto getProfileDetail(Long profileId);
}
