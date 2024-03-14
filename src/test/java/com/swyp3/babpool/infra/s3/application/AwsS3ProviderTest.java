package com.swyp3.babpool.infra.s3.application;

import com.fasterxml.uuid.Generators;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.StringUtils;

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

    @DisplayName("원본 파일명에서 최대 40글자 까지 자르기")
    @Test
    void generateFileName() {
        String originalFileName = "center-username-notion-avatar-1707464623232.png";
        log.info("originalFileName.length() = {}", originalFileName.length());

        String fileName = "static" + "/" + Generators.timeBasedEpochGenerator().generate() + "." + StringUtils.getFilenameExtension(originalFileName);;
        log.info("fileName.length() = {}", fileName.length());

        String finalUrl = "https://babpool-image-bucket.s3.ap-northeast-2.amazonaws.com/" + fileName;
        log.info("finalUrl.length() = {}", finalUrl.length());

        Assertions.assertThat(finalUrl.length()).isLessThanOrEqualTo(150);
        log.info("finalUrl = {}", finalUrl);
    }


}