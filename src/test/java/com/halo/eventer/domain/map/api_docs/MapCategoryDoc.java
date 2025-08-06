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

public class MapCategoryDoc {

    private static final String TAG = "지도 카테고리";

    public static RestDocumentationResultHandler createMapCategory() {
        return document(
                "mapCategory 생성",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("지도 카테고리 생성")
                        .description("특정 축제에 지도 카테고리를 생성합니다.")
                        .pathParameters(parameterWithName("festivalId")
                                .description("축제 id")
                                .attributes(key("validationConstraints").value(List.of(ApiConstraint.JavaxMin2(1)))))
                        .requestFields(fieldWithPath("name")
                                .type(JsonFieldType.STRING)
                                .description("카테고리 이름 (2~10자)")
                                .attributes(key("validationConstraints")
                                        .value(List.of(ApiConstraint.JavaxNotNull(), ApiConstraint.JavaxSize(2, 10)))))
                        .build()));
    }

    public static RestDocumentationResultHandler getMapCategoryImage() {
        return document(
                "mapCategory 이미지 조회 (pin, icon)",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("지도 카테고리에 들어간 이미지 조회 (pin, icon)")
                        .description("지정한 지도 카테고리의 핀/아이콘 이미지를 조회합니다.")
                        .pathParameters(parameterWithName("mapCategoryId")
                                .description("지도 카테고리 id")
                                .attributes(key("validationConstraints").value(List.of(ApiConstraint.JavaxMin2(1)))))
                        .responseFields(
                                fieldWithPath("pin").type(JsonFieldType.STRING).description("핀 이미지 식별자/URL"),
                                fieldWithPath("icon").type(JsonFieldType.STRING).description("아이콘 이미지 식별자/URL"))
                        .build()));
    }

    public static RestDocumentationResultHandler updateMapCategoryImage() {
        return document(
                "mapCategory 이미지 수정",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("지도 카테고리 이미지 수정")
                        .description("지정한 지도 카테고리의 핀/아이콘 이미지를 수정합니다.")
                        .pathParameters(parameterWithName("mapCategoryId")
                                .description("지도 카테고리 id")
                                .attributes(key("validationConstraints").value(List.of(ApiConstraint.JavaxMin2(1)))))
                        .requestFields(
                                fieldWithPath("pin").type(JsonFieldType.STRING).description("핀 이미지 식별자/URL"),
                                fieldWithPath("icon").type(JsonFieldType.STRING).description("아이콘 이미지 식별자/URL"))
                        .build()));
    }

    public static RestDocumentationResultHandler getMapCategoryList() {
        return document(
                "mapCategory 리스트 조회",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("지도 카테고리 리스트 조회")
                        .description("축제별 등록된 지도 카테고리 목록을 조회합니다.")
                        .pathParameters(parameterWithName("festivalId")
                                .description("축제 id")
                                .attributes(key("validationConstraints").value(List.of(ApiConstraint.JavaxMin2(1)))))
                        .responseFields(
                                fieldWithPath("[]").description("지도 카테고리 목록"),
                                fieldWithPath("[].mapCategoryId")
                                        .type(JsonFieldType.NUMBER)
                                        .description("지도 카테고리 id"),
                                fieldWithPath("[].categoryName")
                                        .type(JsonFieldType.STRING)
                                        .description("카테고리 이름"),
                                fieldWithPath("[].pin")
                                        .type(JsonFieldType.STRING)
                                        .description("핀 이미지 식별자/URL"),
                                fieldWithPath("[].icon")
                                        .type(JsonFieldType.STRING)
                                        .description("아이콘 이미지 식별자/URL"),
                                fieldWithPath("[].displayOrder")
                                        .type(JsonFieldType.NUMBER)
                                        .description("노출 순서"))
                        .build()));
    }

    public static RestDocumentationResultHandler deleteMapCategory() {
        return document(
                "mapCategory 삭제",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("지도 카테고리 삭제")
                        .description("지정한 지도 카테고리를 삭제합니다.")
                        .pathParameters(parameterWithName("mapCategoryId")
                                .description("지도 카테고리 id")
                                .attributes(key("validationConstraints").value(List.of(ApiConstraint.JavaxMin2(1)))))
                        .build()));
    }

    public static RestDocumentationResultHandler updateDisplayOrder() {
        return document(
                "mapCategory 순서 수정",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("지도 카테고리 순서 수정")
                        .description("지정한 축제의 지도 카테고리 노출 순서를 일괄 수정합니다.")
                        .pathParameters(parameterWithName("festivalId")
                                .description("축제 id")
                                .attributes(key("validationConstraints").value(List.of(ApiConstraint.JavaxMin2(1)))))
                        .requestFields(
                                fieldWithPath("[].id")
                                        .type(JsonFieldType.NUMBER)
                                        .description("지도 카테고리 id"),
                                fieldWithPath("[].displayOrder")
                                        .type(JsonFieldType.NUMBER)
                                        .description("노출 순서 (<= 11)")
                                        .attributes(key("validationConstraints")
                                                .value(List.of(ApiConstraint.JavaxMax(11)))))
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
