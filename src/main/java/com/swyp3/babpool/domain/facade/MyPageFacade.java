package com.swyp3.babpool.domain.facade;

import com.swyp3.babpool.domain.profile.application.ProfileService;
import com.swyp3.babpool.domain.review.application.ReviewService;
import com.swyp3.babpool.domain.user.application.UserService;
import com.swyp3.babpool.domain.user.application.response.MyPageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MyPageFacade {

    private final UserService userService;
    private final ProfileService profileService;
    private final ReviewService reviewService;

}
