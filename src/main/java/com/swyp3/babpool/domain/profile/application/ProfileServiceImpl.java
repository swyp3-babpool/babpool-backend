package com.swyp3.babpool.domain.profile.application;

import com.swyp3.babpool.domain.profile.api.request.ProfilePagingConditions;
import com.swyp3.babpool.domain.profile.api.request.ProfileUpdateRequest;
import com.swyp3.babpool.domain.profile.application.response.*;
import com.swyp3.babpool.domain.profile.dao.ProfileDetailDaoDto;
import com.swyp3.babpool.domain.profile.dao.ProfileRepository;
import com.swyp3.babpool.domain.profile.domain.Profile;
import com.swyp3.babpool.domain.profile.exception.ProfileException;
import com.swyp3.babpool.domain.profile.exception.errorcode.ProfileErrorCode;
import com.swyp3.babpool.domain.user.dao.UserRepository;
import com.swyp3.babpool.global.common.request.PagingRequestList;
import com.swyp3.babpool.global.uuid.application.UuidService;
import com.swyp3.babpool.infra.s3.application.AwsS3Provider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService{

    private final AwsS3Provider awsS3Provider;
    private final ProfileRepository profileRepository;

    @Override
    public Page<ProfilePagingResponse> getProfileListWithPageable(ProfilePagingConditions profilePagingConditions, Pageable pageable) {
        PagingRequestList<?> pagingRequest = PagingRequestList.builder()
                .condition(profilePagingConditions)
                .pageable(pageable)
                .build();
        List<ProfilePagingResponse> profilePagingResponseList = null;
        int counts = 0;
        try {
            profilePagingResponseList = profileRepository.findAllByPageable(pagingRequest);
            counts = profileRepository.countByPageable(profilePagingConditions);
        } catch (Exception e) {
            log.error("프로필 리스트 조회 중 오류 발생. {}", e.getMessage());
            log.error("{}", e.getStackTrace());
            throw new ProfileException(ProfileErrorCode.PROFILE_LIST_ERROR, "프로필 리스트 조회 중 오류가 발생했습니다.");
        }

        return new PageImpl<>(profilePagingResponseList, pagingRequest.getPageable(), counts);
    }

    @Override
    public ProfileDetailResponse getProfileDetail(Long targetProfileId) {
        if(!isExistProfile(targetProfileId)){
            throw new ProfileException(ProfileErrorCode.PROFILE_TARGET_PROFILE_ERROR,"존재하지 않는 프로필을 조회하였습니다.");
        }
        //review 데이터를 제외한 프로필 상세 데이터
        ProfileDetailDaoDto profileDetailDaoDto = profileRepository.getProfileDetail(targetProfileId);
        //TODO: 후기 데이터와 연결 필요
        ProfileDetailResponse profileDetailResponse = new ProfileDetailResponse(profileDetailDaoDto, null, null);
        return profileDetailResponse;
    }

    private boolean isExistProfile(Long profileId) {
        if(profileRepository.findById(profileId)==null)
            return false;
        return true;
    }

    @Override
    public String uploadProfileImage(Long userId, MultipartFile multipartFile) {
        String uploadedImageUrl = awsS3Provider.uploadImage(multipartFile);
        profileRepository.saveProfileImageUrl(Profile.builder()
                .userId(userId)
                .profileImageUrl(uploadedImageUrl)
                .build());
        return uploadedImageUrl;
    }

    @Override
    public ProfileUpdateResponse saveProfileInfo(Long userId, ProfileUpdateRequest profileUpdateRequest) {
        return null;
    }

}
