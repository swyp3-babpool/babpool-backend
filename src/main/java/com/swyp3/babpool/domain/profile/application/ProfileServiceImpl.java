package com.swyp3.babpool.domain.profile.application;

import com.swyp3.babpool.domain.profile.api.request.ProfileListRequest;
import com.swyp3.babpool.domain.profile.api.request.ProfileUpdateRequest;
import com.swyp3.babpool.domain.profile.application.response.ProfileResponse;
import com.swyp3.babpool.domain.profile.application.response.ProfileUpdateResponse;
import com.swyp3.babpool.domain.profile.dao.ProfileRepository;
import com.swyp3.babpool.domain.profile.domain.Profile;
import com.swyp3.babpool.infra.s3.application.AwsS3Provider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService{

    private final AwsS3Provider awsS3Provider;
    private final ProfileRepository profileRepository;

    @Override
    public List<ProfileResponse> getProfileListInConditionsOf(ProfileListRequest profileListRequest) {
        return profileRepository.findByUserIdAndSearchTermAndKeywords(profileListRequest).stream()
                .map(ProfileResponse::from)
                .toList();
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
