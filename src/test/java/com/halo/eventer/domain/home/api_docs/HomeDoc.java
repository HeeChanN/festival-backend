package com.halo.eventer.domain.home.api_docs;

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

public class HomeDoc {

    private static final String TAG = "메인 화면";

    public static RestDocumentationResultHandler getMainPage() {
        return document(
                "home 메인페이지 조회",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("메인 페이지 데이터 조회")
                        .description("축제 ID로 메인 화면 구성을 위한 데이터를 조회합니다.")
                        .pathParameters(parameterWithName("festivalId")
                                .description("축제 id")
                                .attributes(key("validationConstraints").value(List.of(ApiConstraint.JavaxMin()))))
                        .responseFields(
                                fieldWithPath("upWidgets[]")
                                        .type(JsonFieldType.ARRAY)
                                        .description("상단 위젯 목록"),
                                fieldWithPath("upWidgets[].id")
                                        .type(JsonFieldType.NUMBER)
                                        .description("상단 위젯 id")
                                        .optional(),
                                fieldWithPath("upWidgets[].name")
                                        .type(JsonFieldType.STRING)
                                        .description("상단 위젯 이름")
                                        .optional(),
                                fieldWithPath("upWidgets[].url")
                                        .type(JsonFieldType.STRING)
                                        .description("상단 위젯 링크 URL"),
                                fieldWithPath("upWidgets[].periodStart")
                                        .type(JsonFieldType.STRING)
                                        .description("상단 위젯 노출 시작일시 (ISO-8601, 예: 2025-08-04T10:00:00)"),
                                fieldWithPath("upWidgets[].periodEnd").description("상단 위젯 노출 종료일시 (ISO-8601)"),
                                fieldWithPath("upWidgets[].createdAt").description("상단 위젯 생성일시 (ISO-8601)"),
                                fieldWithPath("upWidgets[].updatedAt").description("상단 위젯 수정일시 (ISO-8601)"),
                                fieldWithPath("banner[]")
                                        .type(JsonFieldType.ARRAY)
                                        .description("배너(공지) 목록"),
                                fieldWithPath("banner[].id")
                                        .type(JsonFieldType.NUMBER)
                                        .description("배너 id"),
                                fieldWithPath("banner[].displayOrder")
                                        .type(JsonFieldType.NUMBER)
                                        .description("배너 노출 순서"),
                                fieldWithPath("banner[].thumbnail")
                                        .type(JsonFieldType.STRING)
                                        .description("배너 썸네일 URL"),
                                fieldWithPath("banner[].type")
                                        .type(JsonFieldType.STRING)
                                        .description("배너 타입 (enum: ArticleType)"),
                                fieldWithPath("mainWidgets[]")
                                        .type(JsonFieldType.ARRAY)
                                        .description("메인 위젯 목록"),
                                fieldWithPath("mainWidgets[].id")
                                        .type(JsonFieldType.NUMBER)
                                        .description("메인 위젯 id"),
                                fieldWithPath("mainWidgets[].name")
                                        .type(JsonFieldType.STRING)
                                        .description("메인 위젯 이름"),
                                fieldWithPath("mainWidgets[].url")
                                        .type(JsonFieldType.STRING)
                                        .description("메인 위젯 링크 URL"),
                                fieldWithPath("mainWidgets[].image")
                                        .type(JsonFieldType.STRING)
                                        .description("메인 위젯 이미지 URL"),
                                fieldWithPath("mainWidgets[].description")
                                        .type(JsonFieldType.STRING)
                                        .description("메인 위젯 설명"),
                                fieldWithPath("middleBanners[]")
                                        .type(JsonFieldType.ARRAY)
                                        .description("중간 배너 목록"),
                                fieldWithPath("middleBanners[].id")
                                        .type(JsonFieldType.NUMBER)
                                        .description("중간 배너 id"),
                                fieldWithPath("middleBanners[].name")
                                        .type(JsonFieldType.STRING)
                                        .description("중간 배너 이름"),
                                fieldWithPath("middleBanners[].url")
                                        .type(JsonFieldType.STRING)
                                        .description("중간 배너 링크 URL"),
                                fieldWithPath("middleBanners[].image")
                                        .type(JsonFieldType.STRING)
                                        .description("중간 배너 이미지 URL"),
                                fieldWithPath("middleBanners[].displayOrder")
                                        .type(JsonFieldType.NUMBER)
                                        .description("중간 배너 노출 순서"),
                                fieldWithPath("squareWidgets[]")
                                        .type(JsonFieldType.ARRAY)
                                        .description("정사각형(카드) 위젯 목록"),
                                fieldWithPath("squareWidgets[].id")
                                        .type(JsonFieldType.NUMBER)
                                        .description("정사각형 위젯 id"),
                                fieldWithPath("squareWidgets[].name")
                                        .type(JsonFieldType.STRING)
                                        .description("정사각형 위젯 이름"),
                                fieldWithPath("squareWidgets[].description")
                                        .type(JsonFieldType.STRING)
                                        .description("정사각형 위젯 설명"),
                                fieldWithPath("squareWidgets[].icon")
                                        .type(JsonFieldType.STRING)
                                        .description("정사각형 위젯 아이콘 URL"),
                                fieldWithPath("squareWidgets[].url")
                                        .type(JsonFieldType.STRING)
                                        .description("정사각형 위젯 링크 URL"),
                                fieldWithPath("squareWidgets[].displayOrder")
                                        .type(JsonFieldType.NUMBER)
                                        .description("정사각형 위젯 노출 순서"),
                                fieldWithPath("downWidgets[]")
                                        .type(JsonFieldType.ARRAY)
                                        .description("하단 위젯 목록"),
                                fieldWithPath("downWidgets[].id")
                                        .type(JsonFieldType.NUMBER)
                                        .description("하단 위젯 id"),
                                fieldWithPath("downWidgets[].name")
                                        .type(JsonFieldType.STRING)
                                        .description("하단 위젯 이름"),
                                fieldWithPath("downWidgets[].url")
                                        .type(JsonFieldType.STRING)
                                        .description("하단 위젯 링크 URL"),
                                fieldWithPath("downWidgets[].displayOrder")
                                        .type(JsonFieldType.NUMBER)
                                        .description("하단 위젯 노출 순서"),
                                fieldWithPath("missingPersonDtos[]")
                                        .type(JsonFieldType.ARRAY)
                                        .description("실종자 팝업 목록"),
                                fieldWithPath("missingPersonDtos[].name")
                                        .type(JsonFieldType.STRING)
                                        .description("실종자 이름"),
                                fieldWithPath("missingPersonDtos[].age")
                                        .type(JsonFieldType.STRING)
                                        .description("실종자 나이"),
                                fieldWithPath("missingPersonDtos[].gender")
                                        .type(JsonFieldType.STRING)
                                        .description("실종자 성별"),
                                fieldWithPath("missingPersonDtos[].thumbnail")
                                        .type(JsonFieldType.STRING)
                                        .description("실종자 썸네일 URL"),
                                fieldWithPath("missingPersonDtos[].content")
                                        .type(JsonFieldType.STRING)
                                        .description("실종자 설명/내용"),
                                fieldWithPath("missingPersonDtos[].missingLocation")
                                        .type(JsonFieldType.STRING)
                                        .description("실종 위치"))
                        .build()));
    }

    public static RestDocumentationResultHandler getFestivalSummaryBySubdomain() {
        return document(
                "home 서브도메인 기반 축제요약 조회",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("서브도메인 기반 축제 요약 조회")
                        .description("subAddress(서브도메인/서브주소)로 축제 요약 정보를 조회합니다.")
                        .queryParameters(parameterWithName("subAddress")
                                .description("축제 서브 주소(서브도메인)")
                                .attributes(key("validationConstraints").value(List.of(ApiConstraint.JavaxNotNull()))))
                        .responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("축제 ID"),
                                fieldWithPath("festivalName")
                                        .type(JsonFieldType.STRING)
                                        .description("축제명"),
                                fieldWithPath("subAddress")
                                        .type(JsonFieldType.STRING)
                                        .description("서브 도메인"),
                                fieldWithPath("latitude")
                                        .type(JsonFieldType.NUMBER)
                                        .description("위도"),
                                fieldWithPath("longitude")
                                        .type(JsonFieldType.NUMBER)
                                        .description("경도"))
                        .build()));
    }

    public static RestDocumentationResultHandler healthCheck() {
        return document(
                "헬스체크",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("헬스 체크")
                        .description("서비스 상태 확인용 엔드포인트입니다.")
                        .build()));
    }
}
