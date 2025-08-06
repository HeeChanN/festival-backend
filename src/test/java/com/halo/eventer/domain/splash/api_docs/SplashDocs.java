package com.halo.eventer.domain.splash.api_docs;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

import com.epages.restdocs.apispec.ResourceSnippetParameters;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.snippet.Attributes.key;

public class SplashDocs {

    private static final String TAG = "Splash";

    public static RestDocumentationResultHandler uploadSplash() {
        return document(
                "splash 이미지 업로드·수정",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("스플래시 이미지 업로드·수정")
                        .description(
                                """
                            특정 축제의 스플래시 이미지를 업로드(또는 교체)함
                            layerType 은 background, top, center, bottom 중 적어도 하나여야 함
                            """)
                        .queryParameters(parameterWithName("festivalId")
                                .description("축제 id")
                                .attributes(key("constraints").value("1 이상의 양의 정수")))
                        .requestFields(
                                fieldWithPath("[].url")
                                        .type(JsonFieldType.STRING)
                                        .description("업로드할 이미지 URL")
                                        .attributes(key("constraints").value("NotBlank")),
                                fieldWithPath("[].layerType")
                                        .type(JsonFieldType.STRING)
                                        .description("레이어 구분 (background | top | center | bottom)")
                                        .attributes(key("constraints").value("NotBlank, 지정된 값만 허용")))
                        .build()));
    }

    public static RestDocumentationResultHandler deleteSplash() {
        return document(
                "splash 이미지 삭제",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("스플래시 이미지 삭제")
                        .description(
                                """
                            특정 축제의 스플래시 이미지 레이어를 삭제함.
                            요청 본문은 삭제할 레이어 타입들의 리스트여야 하며,
                            layerType 값은 background, top, center, bottom 중 하나여야 함.
                            """)
                        .queryParameters(parameterWithName("festivalId")
                                .description("축제 id")
                                .attributes(key("constraints").value("1 이상의 양의 정수")))
                        .requestFields(fieldWithPath("[]")
                                .type(JsonFieldType.ARRAY)
                                .description("삭제할 레이어 타입 (background | top | center | bottom)")
                                .attributes(key("constraints").value("NotBlank, 지정된 값만 허용")))
                        .build()));
    }

    public static RestDocumentationResultHandler getSplash() {
        return document(
                "splash 조회",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("스플레시 조회")
                        .description("축제 id로 스플레시 조회")
                        .queryParameters(parameterWithName("festivalId")
                                .description("축제 id")
                                .attributes(key("constraints").value("1 이상의 양의 정수")))
                        .responseFields(
                                fieldWithPath("background")
                                        .type(JsonFieldType.STRING)
                                        .description("배경 레이어 이미지 URL")
                                        .optional(),
                                fieldWithPath("top")
                                        .type(JsonFieldType.STRING)
                                        .description("상단 레이어(top) 이미지 URL")
                                        .optional(),
                                fieldWithPath("center")
                                        .type(JsonFieldType.STRING)
                                        .description("중앙 레이어(center) 이미지 URL")
                                        .optional(),
                                fieldWithPath("bottom")
                                        .type(JsonFieldType.STRING)
                                        .description("하단 레이어(bottom) 이미지 URL")
                                        .optional())
                        .build()));
    }

    public static RestDocumentationResultHandler errorSnippet(String identifier) {
        return document(
                identifier,
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .responseFields(
                                fieldWithPath("code").description("에러 코드 번호"),
                                fieldWithPath("message").description("상세 에러 메시지"),
                                fieldWithPath("status").description("HTTP 상태 코드"))
                        .build()));
    }
}
