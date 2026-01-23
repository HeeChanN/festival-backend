package com.halo.eventer.domain.stamp.api_docs;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

import com.epages.restdocs.apispec.ResourceSnippetParameters;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;

public class StampDocs {
    private static final String TAG = "스탬프 관리";

    public static RestDocumentationResultHandler registerStamp() {
        return document(
                "stamp-register",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("스탬프 등록")
                        .description("페스티벌 ID를 받아 해당 페스티벌에 스탬프를 등록합니다.")
                        .requestHeaders(headerWithName("Authorization").description("JWT Access 토큰"))
                        .queryParameters(parameterWithName("festivalId")
                                .description("페스티벌 ID")
                                .attributes(key("constraints").value("1 이상의 양의 정수")))
                        .responseFields(
                                fieldWithPath("[].stampId")
                                        .type(JsonFieldType.NUMBER)
                                        .description("스탬프 ID"),
                                fieldWithPath("[].stampOn")
                                        .type(JsonFieldType.BOOLEAN)
                                        .description("스탬프 활성화 여부"),
                                fieldWithPath("[].stampFinishCnt")
                                        .type(JsonFieldType.NUMBER)
                                        .description("스탬프 완료 기준 개수"))
                        .build()));
    }

    public static RestDocumentationResultHandler getStampList() {
        return document(
                "stamp-get-list",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("스탬프 리스트 조회")
                        .description("해당 festivalId에 대한 스탬프 리스트를 조회합니다.")
                        .requestHeaders(headerWithName("Authorization").description("JWT Access 토큰"))
                        .queryParameters(parameterWithName("festivalId").description("축제 ID"))
                        .build()));
    }

    public static RestDocumentationResultHandler updateStampOn() {
        return document(
                "stamp-update-on",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("스탬프 활성화")
                        .description("해당 스탬프를 활성화 상태로 변경합니다.")
                        .requestHeaders(headerWithName("Authorization").description("JWT Access 토큰"))
                        .queryParameters(parameterWithName("stampId").description("스탬프 ID"))
                        .build()));
    }

    public static RestDocumentationResultHandler deleteStamp() {
        return document(
                "stamp-delete",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("스탬프 삭제")
                        .description("해당 스탬프를 삭제합니다.")
                        .requestHeaders(headerWithName("Authorization").description("JWT Access 토큰"))
                        .queryParameters(parameterWithName("stampId").description("스탬프 ID"))
                        .build()));
    }

    public static RestDocumentationResultHandler getMissionList() {
        return document(
                "stamp-get-missions",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("미션 목록 조회")
                        .description("해당 스탬프에 연결된 미션 목록을 반환합니다.")
                        .queryParameters(parameterWithName("stampId").description("스탬프 ID"))
                        .build()));
    }

    public static RestDocumentationResultHandler getStampUsers() {
        return document(
                "stamp-get-users",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("스탬프 참가자 목록 조회")
                        .description("해당 스탬프에 참여한 유저들의 정보를 반환합니다.")
                        .requestHeaders(headerWithName("Authorization").description("JWT Access 토큰"))
                        .queryParameters(parameterWithName("stampId").description("스탬프 ID"))
                        .build()));
    }

    public static RestDocumentationResultHandler setFinishCnt() {
        return document(
                "stamp-set-finish-count",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("완료 기준 미션 수 설정")
                        .description("해당 스탬프의 완료 기준 미션 수(cnt)를 설정합니다.")
                        .requestHeaders(headerWithName("Authorization").description("JWT Access 토큰"))
                        .queryParameters(
                                parameterWithName("stampId").description("스탬프 ID"),
                                parameterWithName("cnt").description("완료로 간주할 미션 수"))
                        .build()));
    }

    public static RestDocumentationResultHandler errorSnippet(String identifier) {
        return document(
                identifier,
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .responseFields(
                                fieldWithPath("code").description("에러 코드"),
                                fieldWithPath("message").description("에러 상세"),
                                fieldWithPath("status").description("HTTP 상태"))
                        .build()));
    }
}
