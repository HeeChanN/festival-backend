package com.halo.eventer.domain.stamp.api_docs;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

import com.epages.restdocs.apispec.ResourceSnippetParameters;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.snippet.Attributes.key;

public class MissionDocs {

    private static final String TAG = "미션";

    public static RestDocumentationResultHandler createMissionList() {
        return document(
                "mission-create-list",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("미션 목록 생성")
                        .description(
                                """
                                스탬프ID에 해당하는 미션들을 일괄 생성합니다.
                                요청 바디는 미션 생성 DTO의 배열입니다.
                                """)
                        .requestHeaders(headerWithName("Authorization").description("JWT Access 토큰"))
                        .queryParameters(parameterWithName("stampId")
                                .description("스탬프 ID")
                                .attributes(key("constraints").value("1 이상의 양의 정수")))
                        .requestFields(
                                fieldWithPath("[].boothId")
                                        .type(JsonFieldType.NUMBER)
                                        .description("부스 ID"),
                                fieldWithPath("[].title")
                                        .type(JsonFieldType.STRING)
                                        .description("미션 제목"),
                                fieldWithPath("[].content")
                                        .type(JsonFieldType.STRING)
                                        .description("미션 설명"),
                                fieldWithPath("[].place")
                                        .type(JsonFieldType.STRING)
                                        .description("장소"),
                                fieldWithPath("[].time")
                                        .type(JsonFieldType.STRING)
                                        .description("운영 시간"),
                                fieldWithPath("[].clearedThumbnail")
                                        .type(JsonFieldType.STRING)
                                        .description("완료 썸네일"),
                                fieldWithPath("[].notClearedThumbnail")
                                        .type(JsonFieldType.STRING)
                                        .description("미완료 썸네일"))
                        .build()));
    }

    public static RestDocumentationResultHandler getMission() {
        return document(
                "mission-get-one",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("미션 단건 조회")
                        .description("missionId로 특정 미션 상세를 조회합니다.")
                        .queryParameters(parameterWithName("missionId")
                                .description("미션 ID")
                                .attributes(key("constraints").value("1 이상의 양의 정수")))
                        .responseFields(
                                fieldWithPath("boothId")
                                        .type(JsonFieldType.NUMBER)
                                        .description("미션 부스 ID"),
                                fieldWithPath("title")
                                        .type(JsonFieldType.STRING)
                                        .description("제목"),
                                fieldWithPath("content")
                                        .type(JsonFieldType.STRING)
                                        .description("내용"),
                                fieldWithPath("place")
                                        .type(JsonFieldType.STRING)
                                        .description("장소"),
                                fieldWithPath("time").type(JsonFieldType.STRING).description("시간"),
                                fieldWithPath("clearedThumbnail")
                                        .type(JsonFieldType.STRING)
                                        .description("완료 썸네일"),
                                fieldWithPath("notClearedThumbnail")
                                        .type(JsonFieldType.STRING)
                                        .description("미완료 썸네일"))
                        .build()));
    }

    public static RestDocumentationResultHandler getMissionList() {
        return document(
                "mission-get-list",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("미션 목록 조회")
                        .description("stampId로 해당 스탬프의 미션 목록(요약)을 조회합니다.")
                        .queryParameters(parameterWithName("stampId")
                                .description("스탬프 ID")
                                .attributes(key("constraints").value("1 이상의 양의 정수")))
                        .responseFields(
                                fieldWithPath("[].missionId")
                                        .type(JsonFieldType.NUMBER)
                                        .description("미션 ID"),
                                fieldWithPath("[].title")
                                        .type(JsonFieldType.STRING)
                                        .description("제목"),
                                fieldWithPath("[].clearedThumbnail")
                                        .type(JsonFieldType.STRING)
                                        .description("완료 썸네일"),
                                fieldWithPath("[].notClearedThumbnail")
                                        .type(JsonFieldType.STRING)
                                        .description("미완료 썸네일"))
                        .build()));
    }

    public static RestDocumentationResultHandler updateMission() {
        return document(
                "mission-update",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("미션 수정")
                        .description(
                                """
                                missionId로 특정 미션을 수정합니다.
                                일부 필드만 보낼 수 있으며, 비어있지 않은 값만 업데이트되는 패치 방식을 가정합니다.
                                """)
                        .requestHeaders(headerWithName("Authorization").description("JWT Access 토큰"))
                        .queryParameters(parameterWithName("missionId")
                                .description("미션 ID")
                                .attributes(key("constraints").value("1 이상의 양의 정수")))
                        .requestFields(
                                fieldWithPath("boothId")
                                        .type(JsonFieldType.NUMBER)
                                        .optional()
                                        .description("부스 ID (선택)"),
                                fieldWithPath("title")
                                        .type(JsonFieldType.STRING)
                                        .optional()
                                        .description("제목 (선택)"),
                                fieldWithPath("content")
                                        .type(JsonFieldType.STRING)
                                        .optional()
                                        .description("내용 (선택)"),
                                fieldWithPath("place")
                                        .type(JsonFieldType.STRING)
                                        .optional()
                                        .description("장소 (선택)"),
                                fieldWithPath("time")
                                        .type(JsonFieldType.STRING)
                                        .optional()
                                        .description("시간 (선택)"),
                                fieldWithPath("clearedThumbnail")
                                        .type(JsonFieldType.STRING)
                                        .optional()
                                        .description("완료 썸네일 (선택)"),
                                fieldWithPath("notClearedThumbnail")
                                        .type(JsonFieldType.STRING)
                                        .optional()
                                        .description("미완료 썸네일 (선택)"))
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
