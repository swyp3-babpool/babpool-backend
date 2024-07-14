package com.swyp3.babpool.domain.profile.dao;

import com.swyp3.babpool.domain.profile.application.response.ProfileKeywordsResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@ActiveProfiles("test")
@MybatisTest
class ProfileRepositoryTest {

    @Autowired
    private ProfileRepository profileRepository;

}