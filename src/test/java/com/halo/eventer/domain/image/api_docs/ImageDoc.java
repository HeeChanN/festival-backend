package com.halo.eventer.domain.image.api_docs;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;

import com.epages.restdocs.apispec.ResourceSnippetParameters;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.*;

public class ImageDoc {

    private static final String TAG = "이미지 업로드";

    public static RestDocumentationResultHandler getPreSignedUploadUrl() {
        return document(
                "upload presigned-url 발급",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("Presigned 업로드 URL 발급")
                        .description(
                                "S3 등 오브젝트 스토리지에 업로드하기 위한 Pre-signed URL을 발급합니다. " + "응답은 text/plain으로 URL 문자열을 반환합니다.")
                        .queryParameters(
                                parameterWithName("fileExtension").description("업로드할 파일 확장자 (예: jpg, jpeg, png, webp)"))
                        .responseHeaders(headerWithName("Content-Type").description("text/plain; charset=UTF-8"))
                        .build()));
    }
}
