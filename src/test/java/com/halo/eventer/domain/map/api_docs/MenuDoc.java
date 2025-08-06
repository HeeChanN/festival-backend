package com.halo.eventer.domain.map.api_docs;

import java.util.List;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.halo.eventer.global.constants.ApiConstraint;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.snippet.Attributes.key;

public class MenuDoc {

    private static final String TAG = "부스 메뉴";

    public static RestDocumentationResultHandler createMenus() {
        return document(
                "menu 생성",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("메뉴 생성")
                        .description("지도의 특정 부스/지도 항목(mapId)에 메뉴를 일괄 등록합니다.")
                        .pathParameters(parameterWithName("mapId")
                                .description("지도(부스) id")
                                .attributes(key("validationConstraints").value(List.of(ApiConstraint.JavaxMin2(1)))))
                        .requestFields(
                                fieldWithPath("[].name")
                                        .type(JsonFieldType.STRING)
                                        .description("메뉴 이름 (1~20자)")
                                        .attributes(key("validationConstraints")
                                                .value(List.of(
                                                        ApiConstraint.JavaxSize(1, 20), ApiConstraint.JavaxNotNull()))),
                                fieldWithPath("[].price")
                                        .type(JsonFieldType.NUMBER)
                                        .description("가격")
                                        .attributes(key("validationConstraints")
                                                .value(List.of(ApiConstraint.JavaxNotNull()))),
                                fieldWithPath("[].summary")
                                        .type(JsonFieldType.STRING)
                                        .description("요약/설명")
                                        .optional(),
                                fieldWithPath("[].image")
                                        .type(JsonFieldType.STRING)
                                        .description("이미지 식별자/URL")
                                        .optional())
                        .build()));
    }

    public static RestDocumentationResultHandler getMenus() {
        return document(
                "menu 리스트 조회",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("메뉴 리스트 조회")
                        .description("지정한 mapId에 등록된 메뉴 목록을 조회합니다.")
                        .pathParameters(parameterWithName("mapId")
                                .description("지도(부스) id")
                                .attributes(key("constraints").value("필수, 1 이상의 양의 정수")))
                        .responseFields(
                                fieldWithPath("[]").description("메뉴 목록"),
                                fieldWithPath("[].id")
                                        .type(JsonFieldType.NUMBER)
                                        .description("메뉴 id"),
                                fieldWithPath("[].name")
                                        .type(JsonFieldType.STRING)
                                        .description("메뉴 이름"),
                                fieldWithPath("[].price")
                                        .type(JsonFieldType.NUMBER)
                                        .description("메뉴 가격"),
                                fieldWithPath("[].image")
                                        .type(JsonFieldType.STRING)
                                        .description("이미지 식별자/URL"),
                                fieldWithPath("[].summary")
                                        .type(JsonFieldType.STRING)
                                        .description("요약/설명"))
                        .build()));
    }

    public static RestDocumentationResultHandler updateMenus() {
        return document(
                "menu 수정",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("메뉴 일괄 수정")
                        .description("지정한 mapId의 메뉴들을 일괄 수정합니다.")
                        .pathParameters(parameterWithName("mapId")
                                .description("지도(부스) id")
                                .attributes(key("validationConstraints").value(List.of(ApiConstraint.JavaxMin2(1)))))
                        .requestFields(
                                fieldWithPath("[].id")
                                        .type(JsonFieldType.NUMBER)
                                        .description("메뉴 id")
                                        .attributes(key("validationConstraints")
                                                .value(List.of(ApiConstraint.JavaxNotNull()))),
                                fieldWithPath("[].name")
                                        .type(JsonFieldType.STRING)
                                        .description("메뉴 이름 (1~20자)")
                                        .attributes(key("validationConstraints")
                                                .value(List.of(ApiConstraint.JavaxSize(1, 20)))),
                                fieldWithPath("[].price")
                                        .type(JsonFieldType.NUMBER)
                                        .description("가격")
                                        .attributes(key("validationConstraints")
                                                .value(List.of(ApiConstraint.JavaxNotNull()))),
                                fieldWithPath("[].image")
                                        .type(JsonFieldType.STRING)
                                        .description("이미지 식별자/URL")
                                        .optional(),
                                fieldWithPath("[].summary")
                                        .type(JsonFieldType.STRING)
                                        .description("요약/설명")
                                        .optional())
                        .responseFields(
                                fieldWithPath("[]").description("수정 결과 목록"),
                                fieldWithPath("[].id")
                                        .type(JsonFieldType.NUMBER)
                                        .description("메뉴 id"),
                                fieldWithPath("[].name")
                                        .type(JsonFieldType.STRING)
                                        .description("메뉴 이름"),
                                fieldWithPath("[].price")
                                        .type(JsonFieldType.NUMBER)
                                        .description("메뉴 가격"),
                                fieldWithPath("[].image")
                                        .type(JsonFieldType.STRING)
                                        .description("이미지 식별자/URL"),
                                fieldWithPath("[].summary")
                                        .type(JsonFieldType.STRING)
                                        .description("요약/설명"))
                        .build()));
    }

    public static RestDocumentationResultHandler deleteMenu() {
        return document(
                "menu 삭제",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("메뉴 삭제")
                        .description("메뉴 id로 특정 메뉴를 삭제합니다.")
                        .pathParameters(parameterWithName("id")
                                .description("메뉴 id")
                                .attributes(key("validationConstraints").value(List.of(ApiConstraint.JavaxMin2(1)))))
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
