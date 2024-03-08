package com.swyp3.babpool.domain.profile.application;

import com.swyp3.babpool.domain.profile.api.request.ProfilePagingConditions;
import com.swyp3.babpool.domain.profile.api.request.ProfileUpdateRequest;
import com.swyp3.babpool.domain.profile.application.response.*;
import com.swyp3.babpool.domain.profile.application.response.ProfileDetailDaoDto;
import com.swyp3.babpool.domain.profile.application.response.ProfilePagingDto;
import com.swyp3.babpool.domain.profile.application.response.ProfilePagingResponse;
import com.swyp3.babpool.domain.profile.dao.ProfileRepository;
import com.swyp3.babpool.domain.profile.domain.PossibleDate;
import com.swyp3.babpool.domain.profile.domain.Profile;
import com.swyp3.babpool.domain.profile.exception.ProfileException;
import com.swyp3.babpool.domain.profile.exception.errorcode.ProfileErrorCode;
import com.swyp3.babpool.domain.review.application.ReviewService;
import com.swyp3.babpool.domain.review.application.response.ReviewCountByTypeResponse;
import com.swyp3.babpool.global.common.request.PagingRequestList;
import com.swyp3.babpool.infra.s3.application.AwsS3Provider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService{

    private final AwsS3Provider awsS3Provider;
    private final ProfileRepository profileRepository;
    private final ReviewService reviewService;
    @Override
    public Page<ProfilePagingResponse> getProfileListWithPageable(ProfilePagingConditions profilePagingConditions, Pageable pageable) {
        PagingRequestList<?> pagingRequest = PagingRequestList.builder()
                .condition(profilePagingConditions)
                .pageable(pageable)
                .build();
        List<ProfilePagingDto> profilePagingDtoList = null;
        int counts = 0;
        try {
            profilePagingDtoList = profileRepository.findAllByPageable(pagingRequest);
            counts = profileRepository.countByPageable(profilePagingConditions);
        } catch (Exception e) {
            log.error("프로필 리스트 조회 중 오류 발생. {}", e.getMessage());
            log.error("{}", e.getStackTrace());
            throw new ProfileException(ProfileErrorCode.PROFILE_LIST_ERROR, "프로필 리스트 조회 중 오류가 발생했습니다.");
        }
        List<ProfilePagingResponse> profilePagingResponse = profilePagingDtoList.stream()
                .map(ProfilePagingResponse::of)
                .toList();

        return new PageImpl<>(profilePagingResponse, pagingRequest.getPageable(), counts);
    }

    @Override
    public ProfileDetailResponse getProfileDetail(Long targetProfileId) {
        if(!isExistProfile(targetProfileId)){
            throw new ProfileException(ProfileErrorCode.PROFILE_TARGET_PROFILE_ERROR,"존재하지 않는 프로필을 조회하였습니다.");
        }

        ProfileDetailDaoDto profileDetailDaoDto = profileRepository.getProfileDetail(targetProfileId);
        ReviewCountByTypeResponse reviewCountByType = reviewService.getReviewCountByType(targetProfileId);
        ProfileDetailResponse profileDetailResponse = new ProfileDetailResponse(profileDetailDaoDto, reviewCountByType);
        return profileDetailResponse;
    }

    @Override
    public ProfileDefaultResponse getProfileDefault(Long userId) {
        Profile profile = profileRepository.findByUserId(userId);
        ProfileDefaultDaoDto daoResponse= profileRepository.getProfileDefault(profile.getProfileId());
        KeywordsResponse keywords = profileRepository.getKeywords(profile.getProfileId());

        return new ProfileDefaultResponse(daoResponse,keywords);
    }

    private boolean isExistProfile(Long profileId) {
        if(profileRepository.findById(profileId)==null)
            return false;
        return true;
    }

    @Override
    public String uploadProfileImage(Long userId, MultipartFile multipartFile) {
        String uploadedImageUrl = awsS3Provider.uploadImage(multipartFile);
        profileRepository.updateProfileImageUrl(Profile.builder()
                .userId(userId)
                .profileImageUrl(uploadedImageUrl)
                .build());
        return uploadedImageUrl;
    }

    @Override
    public void saveProfile(Profile profile) {
        profileRepository.saveProfile(profile);
    }

    @Override
    public Profile getByUserId(Long userId) {
        return profileRepository.findByUserId(userId);
    }

    @Override
    public ProfileUpdateResponse updateProfileInfo(Long userId, ProfileUpdateRequest profileUpdateRequest) {
        Long profileId = profileRepository.findByUserId(userId).getProfileId();
        profileRepository.updateUserAccount(userId,profileUpdateRequest);
        profileRepository.updateProfile(profileId,profileUpdateRequest);
        profileRepository.deleteUserKeywords(userId);
        profileRepository.insertUserKeywords(userId,profileUpdateRequest.getKeywords());
        updatePossibleDateTime(profileId,profileUpdateRequest);
        return new ProfileUpdateResponse(profileId);
    }

    private void updatePossibleDateTime(Long profileId, ProfileUpdateRequest profileUpdateRequest) {
        profileRepository.deletePossibleTimes(profileId);
        profileRepository.deletePossibleDates(profileId);

        for (Map.Entry<String, List<Integer>> entry : profileUpdateRequest.getPossibleDate().entrySet()) {
            String date = entry.getKey();

            PossibleDate possibleDate = PossibleDate.builder()
                    .possible_date(date)
                    .profile_id(profileId)
                    .build();
            profileRepository.insertPossibleDates(possibleDate);

            List<Integer> times = entry.getValue();

            for (Integer time : times) {
                profileRepository.insertPossibleTimes(time, possibleDate.getId());
            }
        }
    }
}