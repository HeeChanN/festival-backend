package com.halo.eventer.domain.parking.api_docs;

import java.util.List;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.halo.eventer.global.constants.ApiConstraint;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.snippet.Attributes.key;

public final class ParkingNoticeDoc {

    private static final String TAG = "주차 공지/안내";

    private ParkingNoticeDoc() {}

    public static RestDocumentationResultHandler errorSnippet(String identifier) {
        return document(
                identifier,
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("에러 응답 형식")
                        .responseFields(
                                fieldWithPath("code").type(JsonFieldType.STRING).description("에러 코드"),
                                fieldWithPath("message")
                                        .type(JsonFieldType.STRING)
                                        .description("상세 메시지"),
                                fieldWithPath("status")
                                        .type(JsonFieldType.NUMBER)
                                        .description("HTTP 상태 코드"))
                        .build()));
    }

    public static RestDocumentationResultHandler create() {
        return document(
                "parking-notice-생성",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("공지 생성 (ADMIN)")
                        .description("특정 주차관리(parkingManagementId)에 공지를 생성합니다.")
                        .requestSchema(Schema.schema("ParkingNoticeReqDto"))
                        .pathParameters(parameterWithName("parkingManagementId")
                                .description("주차 관리 ID")
                                .attributes(key("validationConstraints").value(List.of(ApiConstraint.JavaxMin2(1)))))
                        .requestHeaders(headerWithName("Authorization").description("Bearer {JWT}"))
                        .requestFields(
                                fieldWithPath("title")
                                        .type(JsonFieldType.STRING)
                                        .description("제목")
                                        .attributes(key("constraints").value("필수, NotNull/NotBlank")),
                                fieldWithPath("content")
                                        .type(JsonFieldType.STRING)
                                        .description("내용")
                                        .attributes(key("constraints").value("필수, NotNull/NotBlank")))
                        .build()));
    }

    public static RestDocumentationResultHandler getAdminList() {
        return document(
                "parking-notice-관리자-목록조회",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("공지 목록 조회 (ADMIN)")
                        .description("주차관리 ID에 연결된 전체 공지 목록을 조회합니다.")
                        .pathParameters(parameterWithName("parkingManagementId")
                                .description("주차관리 ID")
                                .attributes(key("validationConstraints").value(List.of(ApiConstraint.JavaxMin2(1)))))
                        .requestHeaders(headerWithName("Authorization").description("Bearer {JWT}"))
                        .responseSchema(Schema.schema("ParkingNoticeResDto[]"))
                        .responseFields(
                                fieldWithPath("[].id")
                                        .type(JsonFieldType.NUMBER)
                                        .description("공지 ID"),
                                fieldWithPath("[].title")
                                        .type(JsonFieldType.STRING)
                                        .description("제목"),
                                fieldWithPath("[].content")
                                        .type(JsonFieldType.STRING)
                                        .description("내용"),
                                fieldWithPath("[].visible")
                                        .type(JsonFieldType.BOOLEAN)
                                        .description("노출 여부"))
                        .build()));
    }

    public static RestDocumentationResultHandler getUserVisibleList() {
        return document(
                "parking-notice-사용자-visible-조회",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("노출 공지 조회 (USER)")
                        .description("사용자는 노출(visible = true)된 공지 목록만 조회합니다.")
                        .pathParameters(parameterWithName("parkingManagementId")
                                .description("주차관리 ID")
                                .attributes(key("validationConstraints").value(List.of(ApiConstraint.JavaxMin2(1)))))
                        .responseSchema(Schema.schema("ParkingNoticeResDto[]"))
                        .responseFields(
                                fieldWithPath("[].id")
                                        .type(JsonFieldType.NUMBER)
                                        .description("공지 ID"),
                                fieldWithPath("[].title")
                                        .type(JsonFieldType.STRING)
                                        .description("제목"),
                                fieldWithPath("[].content")
                                        .type(JsonFieldType.STRING)
                                        .description("내용"),
                                fieldWithPath("[].visible")
                                        .type(JsonFieldType.BOOLEAN)
                                        .description("노출 여부(항상 true)"))
                        .build()));
    }

    public static RestDocumentationResultHandler updateContent() {
        return document(
                "parking-notice-내용수정",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("공지 내용 수정 (ADMIN)")
                        .description("공지의 제목/내용을 수정합니다.")
                        .requestSchema(Schema.schema("ParkingNoticeContentReqDto"))
                        .pathParameters(parameterWithName("id")
                                .description("공지 ID")
                                .attributes(key("validationConstraints").value(List.of(ApiConstraint.JavaxMin2(1)))))
                        .requestHeaders(headerWithName("Authorization").description("Bearer {JWT}"))
                        .requestFields(
                                fieldWithPath("title")
                                        .type(JsonFieldType.STRING)
                                        .description("수정할 제목")
                                        .attributes(key("constraints").value("필수, NotNull/NotBlank")),
                                fieldWithPath("content")
                                        .type(JsonFieldType.STRING)
                                        .description("수정할 내용")
                                        .attributes(key("constraints").value("필수, NotNull/NotBlank")))
                        .build()));
    }

    public static RestDocumentationResultHandler changeVisibility() {
        return document(
                "parking-notice-노출여부변경",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("공지 노출여부 변경 (ADMIN)")
                        .description("공지의 visible 값을 변경합니다.")
                        .requestSchema(Schema.schema("ParkingNoticeVisibilityReqDto"))
                        .pathParameters(parameterWithName("id").description("공지 ID (1 이상)"))
                        .requestHeaders(headerWithName("Authorization").description("Bearer {JWT}"))
                        .requestFields(fieldWithPath("visible")
                                .type(JsonFieldType.BOOLEAN)
                                .description("변경할 노출 여부, (NotNull)"))
                        .build()));
    }

    public static RestDocumentationResultHandler deleteNotice() {
        return document(
                "parking-notice-삭제",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("공지 삭제 (ADMIN)")
                        .description("공지 한 건을 삭제합니다.")
                        .pathParameters(parameterWithName("id").description("공지 ID (1 이상)"))
                        .requestHeaders(headerWithName("Authorization").description("Bearer {JWT}"))
                        .build()));
    }
}
