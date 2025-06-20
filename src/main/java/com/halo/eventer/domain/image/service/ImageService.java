package com.halo.eventer.domain.image.service;

import java.time.Duration;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.f4b6a3.ulid.UlidCreator;
import com.halo.eventer.global.error.ErrorCode;
import com.halo.eventer.global.error.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {

    private static final Map<String, String> EXTENSION_TO_CONTENT_TYPE = Map.of(
            "jpg", "image/jpeg",
            "jpeg", "image/jpeg",
            "png", "image/png",
            "webp", "image/webp");

    private final S3Client s3Client;
    private final S3Presigner presigner;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // TODO : S3 업로드만 되어있고 실제 사용하지 않는 이미지 S3에서 삭제하는 로직 필요 + 동시요청이 오는 환경에서 동시성 제어 필요
    public String generatePreSignedUploadUrl(String fileExtension) {
        String ulid = UlidCreator.getUlid().toString();

        if (!isAllowedExtension(fileExtension)) {
            throw new BaseException(ErrorCode.UNACCEPTABLE_EXTENSION);
        }

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key("uploads/" + ulid + "." + fileExtension)
                .contentType(EXTENSION_TO_CONTENT_TYPE.get(fileExtension))
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .putObjectRequest(objectRequest)
                .signatureDuration(Duration.ofMinutes(15))
                .build();

        return presigner.presignPutObject(presignRequest).url().toString();
    }

    private static boolean isAllowedExtension(String extension) {
        return EXTENSION_TO_CONTENT_TYPE.containsKey(extension.toLowerCase());
    }
}
