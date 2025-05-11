package com.halo.eventer.domain.image.service;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.github.f4b6a3.ulid.UlidCreator;
import com.halo.eventer.global.error.ErrorCode;
import com.halo.eventer.global.error.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.sync.RequestBody;
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

    private static final Map<String, String> CONTENT_TYPE_TO_EXTENSION = Map.of(
            "image/jpeg", "jpg",
            "image/png", "png",
            "image/webp", "webp");

    private final S3Client s3Client;
    private final S3Presigner presigner;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadStream(InputStream inputStream, long contentLength, String contentType) throws IOException {

        String fileExtension = CONTENT_TYPE_TO_EXTENSION.get(contentType);
        String objectKey = "test/file_" + UlidCreator.getUlid().toString() + "." + fileExtension;
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(objectKey)
                .contentLength(contentLength)
                .contentType(contentType)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, contentLength));

        return s3Client.utilities().getUrl(b -> b.bucket(bucket).key(objectKey)).toString();
    }

    public String upload(MultipartFile multipartFile) throws IOException {

        if (!isAllowedExtension(FilenameUtils.getExtension(multipartFile.getOriginalFilename()))) {
            throw new BaseException(ErrorCode.UNACCEPTABLE_EXTENSION);
        }
        return uploadFileToS3Direct(multipartFile);
    }

    private String uploadFileToS3Direct(MultipartFile uploadFile) throws IOException {

        String objectKey = "test/file_"
                + UlidCreator.getUlid().toString()
                + "."
                + FilenameUtils.getExtension(uploadFile.getOriginalFilename());

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(objectKey)
                .contentType(uploadFile.getContentType())
                .contentLength(uploadFile.getSize())
                .build();

        try (InputStream inputStream = uploadFile.getInputStream()) {
            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, uploadFile.getSize()));
        }
        return s3Client.utilities().getUrl(b -> b.bucket(bucket).key(objectKey)).toString(); // 업로드된 파일의 S3 URL 주소 반환
    }

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

    // Extension 검증 메서드
    public static boolean isAllowedExtension(String extension) {
        return EXTENSION_TO_CONTENT_TYPE.containsKey(extension.toLowerCase());
    }

    // Content-Type 반환 메서드
    public static String getContentType(String extension) {
        String lowerExt = extension.toLowerCase();
        if (!EXTENSION_TO_CONTENT_TYPE.containsKey(lowerExt)) {
            throw new IllegalArgumentException("Unsupported file extension: " + extension);
        }
        return EXTENSION_TO_CONTENT_TYPE.get(lowerExt);
    }
}
