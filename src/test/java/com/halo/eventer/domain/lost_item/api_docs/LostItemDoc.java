package com.halo.eventer.domain.lost_item.api_docs;

import java.util.List;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.halo.eventer.global.constants.ApiConstraint;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.*;
import static com.epages.restdocs.apispec.ResourceDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.snippet.Attributes.key;

public class LostItemDoc {

    private static final String TAG = "분실물";

    public static RestDocumentationResultHandler createLostItem() {
        return document(
                "lostItem 생성",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("분실물 등록")
                        .description("특정 축제에 분실물을 등록합니다.")
                        .requestHeaders(headerWithName("Authorization").description("JWT Access 토큰"))
                        .queryParameters(parameterWithName("festivalId")
                                .description("축제 id (1 이상 정수)")
                                .attributes(key("validationConstraints").value(List.of(ApiConstraint.JavaxMin()))))
                        .requestFields(
                                fieldWithPath("name")
                                        .type(JsonFieldType.STRING)
                                        .description("분실물 이름")
                                        .attributes(key("validationConstraints")
                                                .value(List.of(ApiConstraint.JavaxNotNull()))),
                                fieldWithPath("description")
                                        .type(JsonFieldType.STRING)
                                        .description("분실물 설명")
                                        .optional(),
                                fieldWithPath("thumbnail")
                                        .type(JsonFieldType.STRING)
                                        .description("분실물 썸네일 이미지 URL")
                                        .optional(),
                                fieldWithPath("findDate")
                                        .type(JsonFieldType.STRING)
                                        .description("발견 날짜 (yyyy-MM-dd)")
                                        .attributes(key("validationConstraints")
                                                .value(List.of(ApiConstraint.JavaxNotNull()))))
                        .build()));
    }

    public static RestDocumentationResultHandler updateLostItem() {
        return document(
                "lostItem 수정",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("분실물 수정")
                        .description("분실물 정보를 수정합니다.")
                        .requestHeaders(headerWithName("Authorization").description("JWT Access 토큰"))
                        .queryParameters(parameterWithName("id")
                                .description("분실물 id")
                                .attributes(key("validationConstraints")
                                        .value(List.of(ApiConstraint.JavaxNotNull(), ApiConstraint.JavaxMin()))))
                        .requestFields(
                                fieldWithPath("name")
                                        .type(JsonFieldType.STRING)
                                        .description("분실물 이름")
                                        .attributes(key("validationConstraints")
                                                .value(List.of(ApiConstraint.JavaxNotNull()))),
                                fieldWithPath("description")
                                        .type(JsonFieldType.STRING)
                                        .description("분실물 설명")
                                        .optional(),
                                fieldWithPath("thumbnail")
                                        .type(JsonFieldType.STRING)
                                        .description("썸네일 이미지 URL")
                                        .optional(),
                                fieldWithPath("findDate")
                                        .type(JsonFieldType.STRING)
                                        .description("발견 날짜 (yyyy-MM-dd)")
                                        .attributes(key("validationConstraints")
                                                .value(List.of(ApiConstraint.JavaxNotNull()))))
                        .responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("분실물 식별자(ID)"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("분실물 이름"),
                                fieldWithPath("description")
                                        .type(JsonFieldType.STRING)
                                        .description("분실물 설명"),
                                fieldWithPath("thumbnail")
                                        .type(JsonFieldType.STRING)
                                        .description("썸네일 이미지 URL"),
                                fieldWithPath("findDate")
                                        .type(JsonFieldType.STRING)
                                        .description("발견 날짜 (yyyy-MM-dd)"))
                        .build()));
    }

    public static RestDocumentationResultHandler deleteLostItem() {
        return document(
                "lostItem 삭제",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("분실물 삭제")
                        .description("분실물을 삭제합니다.")
                        .requestHeaders(headerWithName("Authorization").description("JWT Access 토큰"))
                        .queryParameters(parameterWithName("id")
                                .description("분실물 id")
                                .attributes(key("validationConstraints")
                                        .value(List.of(ApiConstraint.JavaxNotNull(), ApiConstraint.JavaxMin()))))
                        .build()));
    }

    public static RestDocumentationResultHandler getLostItem() {
        return document(
                "lostItem 단일 조회",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("분실물 단일 조회")
                        .description("분실물 ID로 상세 정보를 조회합니다.")
                        .pathParameters(parameterWithName("id")
                                .description("분실물 id")
                                .attributes(key("constraints").value("필수, 1 이상의 양의 정수")))
                        .responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("분실물 식별자(ID)"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("분실물 이름"),
                                fieldWithPath("description")
                                        .type(JsonFieldType.STRING)
                                        .description("분실물 설명"),
                                fieldWithPath("thumbnail")
                                        .type(JsonFieldType.STRING)
                                        .description("썸네일 이미지 URL"),
                                fieldWithPath("findDate")
                                        .type(JsonFieldType.STRING)
                                        .description("발견 날짜 (yyyy-MM-dd)"))
                        .build()));
    }

    public static RestDocumentationResultHandler getLostItemsByFestival() {
        return document(
                "lostItem 리스트 조회",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("분실물 목록 조회")
                        .description("축제 ID로 등록된 분실물 목록을 조회합니다.")
                        .pathParameters(parameterWithName("festivalId")
                                .description("축제 id")
                                .attributes(key("constraints").value("필수, 1 이상의 양의 정수")))
                        .responseFields(
                                fieldWithPath("[]").description("분실물 요약 목록"),
                                fieldWithPath("[].id")
                                        .type(JsonFieldType.NUMBER)
                                        .description("분실물 식별자(ID)"),
                                fieldWithPath("[].name")
                                        .type(JsonFieldType.STRING)
                                        .description("분실물 이름"),
                                fieldWithPath("[].description")
                                        .type(JsonFieldType.STRING)
                                        .description("분실물 설명"))
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
