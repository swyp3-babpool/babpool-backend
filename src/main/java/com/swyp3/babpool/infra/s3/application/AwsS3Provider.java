package com.swyp3.babpool.infra.s3.application;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.fasterxml.uuid.Generators;
import com.swyp3.babpool.infra.s3.exception.AwsS3ErrorCode;
import com.swyp3.babpool.infra.s3.exception.AwsS3Exception;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AwsS3Provider {
    private static final String S3_BUCKET_DIRECTORY_NAME = "static";
    private static final Map<String, Boolean> S3_ALLOWED_IMAGE_FILE_TYPES = Map.of(
            "image/jpeg", true,
            "image/png", true,
            "image/gif", true
    );
    private static final Integer S3_MAX_IMAGE_FILE_SIZE = 5_000_000; // 5MB
    private final AmazonS3 amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;


    public String uploadImage(@NotNull MultipartFile multipartFile) {
        validateImageFileType(multipartFile.getContentType());
        validateImageFileSize(multipartFile.getSize());

        String fileName = generateFileName(multipartFile.getOriginalFilename());
        uploadToS3Bucket(multipartFile, fileName);

        String imageUrl = amazonS3Client.getUrl(bucket, fileName).toString();
        log.info("S3 파일 업로드에 성공. URL: {}", imageUrl);
        return imageUrl;
    }

    private void uploadToS3Bucket(MultipartFile multipartFile, String fileName) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getSize());

        try (InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            log.error("S3 파일 업로드에 실패했습니다. {}", e.getMessage());
            throw new AwsS3Exception(AwsS3ErrorCode.AWS_S3_IMAGE_UPLOAD_FAIL,
                    "Aws s3 image upload fail, in AwsS3Uploader.uploadImage() method.");
        }
    }

    private String generateFileName(String originalFileName) {
        return S3_BUCKET_DIRECTORY_NAME + "/" + Generators.timeBasedEpochGenerator().generate() + "." + StringUtils.getFilenameExtension(originalFileName);
    }

    private void validateImageFileType(String targetContentType) {
        if (!S3_ALLOWED_IMAGE_FILE_TYPES.containsKey(targetContentType)) {
            throw new AwsS3Exception(AwsS3ErrorCode.AWS_S3_INVALID_FILE_TYPE,
                    "Invalid file type. Only JPEG and PNG are supported.");
        }
    }

    private void validateImageFileSize(Long targetSize) {
        if (targetSize > S3_MAX_IMAGE_FILE_SIZE) { // 5MB
            throw new AwsS3Exception(AwsS3ErrorCode.AWS_S3_FILE_TOO_LARGE,
                    "File size is too large. Maximum allowed size is 5MB.");
        }
    }

    public boolean deleteImage(String imageUrlFromProfileRepository) {
        String fileName = S3_BUCKET_DIRECTORY_NAME + imageUrlFromProfileRepository.substring(imageUrlFromProfileRepository.lastIndexOf("/"));
        try {
            amazonS3Client.deleteObject(bucket, fileName);
        } catch (Exception e) {
            log.error("S3 파일 삭제에 실패했습니다. {}", e.getMessage());
            throw new AwsS3Exception(AwsS3ErrorCode.AWS_S3_IMAGE_DELETE_FAIL,
                    "Aws s3 image delete fail, in AwsS3Uploader.deleteImage() method.");
        }
        return true;
    }

    public String getAmazonS3ClientUrlPrefix() {
        // "https://babpool-image-bucket.s3.ap-northeast-2.amazonaws.com/static/"
        return amazonS3Client.getUrl(bucket, S3_BUCKET_DIRECTORY_NAME).toString();
    }
}
