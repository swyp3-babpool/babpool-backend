package com.swyp3.babpool.domain.profile.dao;

import com.swyp3.babpool.domain.profile.api.request.ProfileListRequest;
import com.swyp3.babpool.domain.profile.domain.Profile;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProfileRepository {

    void saveProfileImageUrl(Profile profile);

    List<Profile> findByUserIdAndSearchTermAndKeywords(ProfileListRequest profileListRequest);
}
