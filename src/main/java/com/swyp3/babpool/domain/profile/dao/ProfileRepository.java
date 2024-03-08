package com.swyp3.babpool.domain.profile.dao;

import com.swyp3.babpool.domain.profile.api.request.ProfilePagingConditions;
import com.swyp3.babpool.domain.profile.api.request.ProfileUpdateRequest;
import com.swyp3.babpool.domain.profile.application.response.ProfileKeywordsResponse;
import com.swyp3.babpool.domain.profile.domain.ProfileDefault;
import com.swyp3.babpool.domain.profile.domain.ProfileDetail;
import com.swyp3.babpool.domain.profile.application.response.ProfilePagingDto;
import com.swyp3.babpool.domain.profile.domain.PossibleDate;
import com.swyp3.babpool.domain.profile.domain.Profile;
import com.swyp3.babpool.global.common.request.PagingRequestList;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProfileRepository {

    void saveProfile(Profile profile);
    void updateProfileImageUrl(Profile profile);

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
    List<ProfilePagingDto> findAllByPageable(PagingRequestList<?> pagingRequestList);

    /**
     * 페이징 처리를 위한 카운트 조회
     * @param profilePagingConditions
     *   - search : 검색어 (한 줄 소개, 자기소개 본문)
     *   - userGrades : 사용자 구분, [FIRST_GRADE, SECOND_GRADE, THIRD_GRADE, FOURTH_GRADE, GRADUATE, POST_GRADUATE]
     *   - keywords : 키워드 식별 값 리스트
     * @return 조건 검색 결과 개수
     */
    int countByPageable(ProfilePagingConditions profilePagingConditions);

    /**
     * 프로필 정보 조회
     * @param targetProfileId : 조회 대상 프로필 식별 값
     * @return
     */
    Long findUserIdByProfileId(Long targetProfileId);

    /**
     * 특정 사용자의 특정 시간에 확정된 약속이 있는지 조회
     * @param targetReceiverUserId
     * @param possibleTimeIdList
     * @return
     */
    Integer countAcceptedAppointmentByReceiverIdAndPossibleTimeId(@Param("targetReceiverUserId") Long targetReceiverUserId,
                                                                  @Param("possibleTimeIdList") List<Long> possibleTimeIdList);

    Profile findById(Long profileId);
    Profile findByUserId(Long userId);
    ProfileDetail findProfileDetail(Long profileId);

    void updateUserAccount(@Param("userId") Long userId,@Param("request") ProfileUpdateRequest profileUpdateRequest);
    void updateProfile(@Param("profileId") Long profileId,@Param("request") ProfileUpdateRequest profileUpdateRequest);
    void deleteUserKeywords(Long userId);
    void saveUserKeywords(@Param("userId")Long userId,@Param("keywords") List<String> keywords);
    void deletePossibleDates(Long profileId);
    void savePossibleDates(PossibleDate possibleDate);
    void deletePossibleTimes(Long profileId);
    void savePossibleTimes(@Param("possibleTimeStart") Integer possibleTimeStart,@Param("possibleDateId") Long possibleDateId);
    ProfileKeywordsResponse findKeywords(Long profileId);
    ProfileDefault findProfileDefault(Long profileId);
}
