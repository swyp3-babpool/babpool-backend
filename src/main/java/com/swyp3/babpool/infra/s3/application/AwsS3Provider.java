package com.swyp3.babpool.infra.s3.application;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.fasterxml.uuid.Generators;
import com.swyp3.babpool.infra.s3.exception.AwsS3ErrorCode;
import com.swyp3.babpool.infra.s3.exception.AwsS3Exception;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Component
@RequiredArgsConstructor
public class AwsS3Provider {
    private static final String S3_BUCKET_DIRECTORY_NAME = "static";

    private final AmazonS3 amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


    public String uploadImage(MultipartFile multipartFile) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getSize());

        String fileName = S3_BUCKET_DIRECTORY_NAME + "/" + Generators.timeBasedEpochGenerator().generate() + "." + multipartFile.getOriginalFilename();

        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            log.error("S3 파일 업로드에 실패했습니다. {}", e.getMessage());
            throw new AwsS3Exception(AwsS3ErrorCode.AWS_S3_IMAGE_UPLOAD_FAIL, "Aws s3 image upload fail, in AwsS3Uploader.uploadImage() method.");
        }
        log.info("S3 파일 업로드에 성공했습니다. 파일명: {}", fileName);
        String imageUrl = amazonS3Client.getUrl(bucket, fileName).toString();
        log.info("S3 파일 URL: {}", imageUrl);
        return imageUrl;
    }



}
