package com.swyp3.babpool.infra.s3.application;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class AwsS3ProviderTest {

    @DisplayName("S3 이미지 삭제 요청 URL에서 파일명 추출 테스트")
    @Test
    void extractFileNameFromDeleteRequestUrl() {

        String imageUrlFromProfileRepository = "https://babpool-image-bucket.s3.ap-northeast-2.amazonaws.com/static/018df2dd-6b59-7758-9e76-2862767ce099.center-username-notion-avatar-1707464623232.png";
        String fileName = "static" + imageUrlFromProfileRepository.substring(imageUrlFromProfileRepository.lastIndexOf("/"));
        log.info("fileName = {}", fileName);
    }


}