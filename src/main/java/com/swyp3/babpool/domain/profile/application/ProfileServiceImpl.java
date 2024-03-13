package com.swyp3.babpool.domain.profile.application;

import com.swyp3.babpool.domain.profile.api.request.ProfilePagingConditions;
import com.swyp3.babpool.domain.profile.api.request.ProfileUpdateRequest;
import com.swyp3.babpool.domain.profile.application.response.*;
import com.swyp3.babpool.domain.profile.domain.ProfileDefault;
import com.swyp3.babpool.domain.profile.domain.ProfileDetail;
import com.swyp3.babpool.domain.profile.application.response.ProfilePagingDto;
import com.swyp3.babpool.domain.profile.application.response.ProfilePagingResponse;
import com.swyp3.babpool.domain.profile.dao.ProfileRepository;
import com.swyp3.babpool.domain.profile.domain.PossibleDate;
import com.swyp3.babpool.domain.profile.domain.Profile;
import com.swyp3.babpool.domain.profile.exception.ProfileException;
import com.swyp3.babpool.domain.profile.exception.errorcode.ProfileErrorCode;
import com.swyp3.babpool.domain.review.application.ReviewService;
import com.swyp3.babpool.domain.review.application.response.ReviewCountByTypeResponse;
import com.swyp3.babpool.domain.review.application.response.ReviewPagingResponse;
import com.swyp3.babpool.global.common.request.PagingRequestList;
import com.swyp3.babpool.infra.s3.application.AwsS3Provider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional
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
        ProfileDetail profileDetail = profileRepository.findProfileDetail(targetProfileId);
        ReviewCountByTypeResponse reviewCountByType = reviewService.getReviewCountByType(targetProfileId);
        List<ReviewPagingResponse> reviewListForProfileDetail = reviewService.getReviewListForProfileDetail(targetProfileId, 3);
        ProfileDetailResponse profileDetailResponse = new ProfileDetailResponse(profileDetail, reviewCountByType,reviewListForProfileDetail);
        return profileDetailResponse;
    }

    @Override
    public ProfileDefaultResponse getProfileDefault(Long userId) {
        Profile profile = profileRepository.findByUserId(userId);
        ProfileDefault daoResponse= profileRepository.findProfileDefault(profile.getProfileId());
        ProfileKeywordsResponse keywords = profileRepository.findKeywords(profile.getProfileId());

        return new ProfileDefaultResponse(daoResponse,keywords);
    }

    @Override
    public ProfileRegistrationResponse getProfileisRegistered(Long userId) {
        Profile profile = profileRepository.findByUserId(userId);
        Boolean isRegistered = profileRepository.findProfileIsRegistered(profile.getProfileId());

        return new ProfileRegistrationResponse(isRegistered);
    }

    private boolean isExistProfile(Long profileId) {
        if(profileRepository.findById(profileId)==null)
            return false;
        return true;
    }

    @Override
    public String updateProfileImage(Long userId, MultipartFile multipartFile) {
        if(verifyNoImageFile(multipartFile)){
            return null;
        }

        deleteExistImageIfUserSelfUploaded(userId);
        String uploadedImageUrl = awsS3Provider.uploadImage(multipartFile);

        profileRepository.updateProfileImageUrl(Profile.builder()
                .userId(userId)
                .profileImageUrl(uploadedImageUrl)
                .build());
        return uploadedImageUrl;
    }

    private void deleteExistImageIfUserSelfUploaded(Long userId) {
        Profile targetProfile = profileRepository.findByUserId(userId);
        // 만약 프로필 이미지가 없다면 삭제할 필요가 없다.
        if(StringUtils.hasText(targetProfile.getProfileImageUrl())){
            return;
        }
        // 만약 프로필 이미지 URL 이 S3에 저장된 이미지 URL이 아니라면(카카오,구글 CDN) 삭제할 필요가 없다.
        if(!targetProfile.getProfileImageUrl().startsWith(awsS3Provider.getAmazonS3ClientUrlPrefix())){
            log.info("ProfileService.deleteExistImageIfUserSelfUploaded, S3에 저장된 이미지가 아닙니다. URL: {}",targetProfile.getProfileImageUrl());
            return;
        }
        awsS3Provider.deleteImage(targetProfile.getProfileImageUrl());
    }

    private boolean verifyNoImageFile(MultipartFile multipartFile) {
        if(multipartFile == null || multipartFile.isEmpty()){
            log.info("ProfileService.checkNoImageFile, 첨부된 이미지 파일이 없습니다.");
            return true;
        }
        return false;
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
        validateRequestPossibleDateTime(profileUpdateRequest.getPossibleDate().get("possibleDate"));
        Long profileId = profileRepository.findByUserId(userId).getProfileId();
        profileRepository.updateUserAccount(userId,profileUpdateRequest);
        profileRepository.updateProfile(profileId,profileUpdateRequest);
        profileRepository.deleteUserKeywords(userId);
        profileRepository.saveUserKeywords(userId,profileUpdateRequest.getKeywords());
        updatePossibleDateTime(profileId,profileUpdateRequest);
        return new ProfileUpdateResponse(profileId);
    }

    private void validateRequestPossibleDateTime(List<Integer> possibleTimeList) {
        if(possibleTimeList.size() < 1){
            throw new ProfileException(ProfileErrorCode.PROFILE_POSSIBLE_DATE_ERROR,"가능한 날짜와 시간을 최소 1개 이상 선택해주세요.");
        }
        // time : 8 ~ 22 only
        possibleTimeList.stream()
                .filter(time -> time < 8 || time > 22)
                .findAny()
                .ifPresent(time -> {
                    throw new ProfileException(ProfileErrorCode.PROFILE_POSSIBLE_DATE_ERROR,"가능한 시간은 8시부터 22시까지만 선택 가능합니다.");
                });
    }

    public void updatePossibleDateTime(Long profileId, ProfileUpdateRequest profileUpdateRequest) {
        profileRepository.deletePossibleTimes(profileId);
        profileRepository.deletePossibleDates(profileId);

        for (Map.Entry<String, List<Integer>> entry : profileUpdateRequest.getPossibleDate().entrySet()) {
            String date = entry.getKey();

            PossibleDate possibleDate = PossibleDate.builder()
                    .possible_date(date)
                    .profile_id(profileId)
                    .build();
            profileRepository.savePossibleDates(possibleDate);

            List<Integer> times = entry.getValue();

            for (Integer time : times) {
                profileRepository.savePossibleTimes(time, possibleDate.getId());
            }
        }
    }
}