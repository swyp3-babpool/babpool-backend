package com.swyp3.babpool.domain.facade;

import com.swyp3.babpool.domain.possibledatetime.application.PossibleDateTimeService;
import com.swyp3.babpool.domain.profile.application.ProfileService;
import com.swyp3.babpool.domain.profile.domain.Profile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProfilePossibleDateTimeFacade {

    private final ProfileService profileService;
    private final PossibleDateTimeService possibleDateTimeService;

    public Long getProfileByProfileId(Long targetProfileId) {
        Profile profileByProfileId = profileService.getProfileByProfileId(targetProfileId);
        return profileByProfileId.getUserId();
    }
}
