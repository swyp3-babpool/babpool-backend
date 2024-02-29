package com.swyp3.babpool.infra.health.api;

import com.swyp3.babpool.global.common.response.ApiResponse;
import com.swyp3.babpool.infra.s3.application.AwsS3Provider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class HealthCheckS3Api {

    private final AwsS3Provider awsS3Provider;

    @PostMapping("/api/test/image/upload")
    public ApiResponse<String> testImageUpload(@RequestPart("profileImageFile")MultipartFile multipartFile){
        return ApiResponse.ok(awsS3Provider.uploadImage(multipartFile));
    }
}
