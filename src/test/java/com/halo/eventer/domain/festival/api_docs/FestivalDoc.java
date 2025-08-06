package com.halo.eventer.domain.festival.api_docs;

import java.util.List;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.halo.eventer.global.constants.ApiConstraint;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.snippet.Attributes.key;

public class FestivalDoc {

    private static final String TAG = "축제";

    public static RestDocumentationResultHandler createFestival() {
        return document(
                "festival 생성",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("축제 생성")
                        .description("축제를 생성합니다.")
                        .requestHeaders(headerWithName("Authorization").description("JWT Access 토큰"))
                        .requestFields(
                                fieldWithPath("name")
                                        .type(JsonFieldType.STRING)
                                        .description("축제명")
                                        .attributes(key("validationConstraints")
                                                .value(List.of(ApiConstraint.JavaxNotNull()))),
                                fieldWithPath("subAddress")
                                        .type(JsonFieldType.STRING)
                                        .description("축제에 사용할 서브 도메인")
                                        .attributes(key("validationConstraints")
                                                .value(List.of(ApiConstraint.JavaxNotNull()))))
                        .build()));
    }

    public static RestDocumentationResultHandler updateFestival() {
        return document(
                "festival 수정",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("축제 수정")
                        .description("축제 정보를 수정합니다.")
                        .requestHeaders(headerWithName("Authorization").description("JWT Access 토큰"))
                        .queryParameters(parameterWithName("id")
                                .description("축제 id")
                                .attributes(key("validationConstraints").value(List.of(ApiConstraint.JavaxMin()))))
                        .requestFields(
                                fieldWithPath("name")
                                        .type(JsonFieldType.STRING)
                                        .description("수정할 축제명")
                                        .attributes(key("validationConstraints")
                                                .value(List.of(ApiConstraint.JavaxNotNull()))),
                                fieldWithPath("subAddress")
                                        .type(JsonFieldType.STRING)
                                        .description("축제에 사용할 서브 도메인")
                                        .attributes(key("validationConstraints")
                                                .value(List.of(ApiConstraint.JavaxNotNull()))))
                        .responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("축제 id"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("축제명"),
                                fieldWithPath("logo").type(JsonFieldType.STRING).description("로고 파일 식별용 URL"),
                                fieldWithPath("colors.mainColor")
                                        .type(JsonFieldType.STRING)
                                        .description("메인 색상 (#RGB | #RRGGBB)"),
                                fieldWithPath("colors.backgroundColor")
                                        .type(JsonFieldType.STRING)
                                        .description("배경 색상 (#RGB | #RRGGBB)"),
                                fieldWithPath("colors.subColor")
                                        .type(JsonFieldType.STRING)
                                        .description("서브 색상 (#RGB | #RRGGBB)"),
                                fieldWithPath("colors.fontColor")
                                        .type(JsonFieldType.STRING)
                                        .description("폰트 색상 (#RGB | #RRGGBB)"),
                                fieldWithPath("latitude")
                                        .type(JsonFieldType.NUMBER)
                                        .description("위도(소수)"),
                                fieldWithPath("longitude")
                                        .type(JsonFieldType.NUMBER)
                                        .description("경도(소수)"))
                        .build()));
    }

    public static RestDocumentationResultHandler deleteFestival() {
        return document(
                "festival 삭제",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("축제 삭제")
                        .description("축제를 삭제합니다.")
                        .requestHeaders(headerWithName("Authorization").description("JWT Access 토큰"))
                        .queryParameters(parameterWithName("id")
                                .description("축제 id")
                                .attributes(key("validationConstraints").value(List.of(ApiConstraint.JavaxMin()))))
                        .build()));
    }

    public static RestDocumentationResultHandler updateColor() {
        return document(
                "festival 색상 수정",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("축제 색상 변경")
                        .description("메인/배경/서브/폰트 색상을 변경합니다.")
                        .requestHeaders(headerWithName("Authorization").description("JWT Access 토큰"))
                        .pathParameters(parameterWithName("festivalId")
                                .description("축제 id")
                                .attributes(key("constraints").value("필수, 1 이상의 양의 정수")))
                        .requestFields(
                                fieldWithPath("mainColor")
                                        .type(JsonFieldType.STRING)
                                        .description("메인 색상 (형식: #RGB 또는 #RRGGBB)")
                                        .attributes(key("validationConstraints")
                                                .value(List.of(ApiConstraint.JavaxPattern(
                                                        "^#(?:[0-9A-Fa-f]{3}|[0-9A-Fa-f]{6})$"))))
                                        .optional(),
                                fieldWithPath("backgroundColor")
                                        .type(JsonFieldType.STRING)
                                        .description("배경 색상 (형식: #RGB 또는 #RRGGBB)")
                                        .attributes(key("validationConstraints")
                                                .value(List.of(ApiConstraint.JavaxPattern(
                                                        "^#(?:[0-9A-Fa-f]{3}|[0-9A-Fa-f]{6})$"))))
                                        .optional(),
                                fieldWithPath("subColor")
                                        .type(JsonFieldType.STRING)
                                        .description("서브 색상 (형식: #RGB 또는 #RRGGBB)")
                                        .attributes(key("validationConstraints")
                                                .value(List.of(ApiConstraint.JavaxPattern(
                                                        "^#(?:[0-9A-Fa-f]{3}|[0-9A-Fa-f]{6})$"))))
                                        .optional(),
                                fieldWithPath("fontColor")
                                        .type(JsonFieldType.STRING)
                                        .description("폰트 색상 (형식: #RGB 또는 #RRGGBB)")
                                        .attributes(key("validationConstraints")
                                                .value(List.of(ApiConstraint.JavaxPattern(
                                                        "^#(?:[0-9A-Fa-f]{3}|[0-9A-Fa-f]{6})$"))))
                                        .optional())
                        .build()));
    }

    public static RestDocumentationResultHandler updateLogo() {
        return document(
                "festival 로고 수정",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("축제 로고 변경")
                        .description("로고를 식별하는 URL을 갱신합니다.")
                        .requestHeaders(headerWithName("Authorization").description("JWT Access 토큰"))
                        .pathParameters(parameterWithName("festivalId")
                                .description("축제 id")
                                .attributes(key("constraints").value("필수, 1 이상의 양의 정수")))
                        .requestFields(fieldWithPath("url")
                                .type(JsonFieldType.STRING)
                                .description("로고 파일 식별자 또는 URL")
                                .optional())
                        .build()));
    }

    public static RestDocumentationResultHandler updateLocation() {
        return document(
                "festival 위치 수정",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("축제 위치정보 변경")
                        .description("위도/경도를 갱신합니다.")
                        .requestHeaders(headerWithName("Authorization").description("JWT Access 토큰"))
                        .pathParameters(parameterWithName("festivalId")
                                .description("축제 id")
                                .attributes(key("constraints").value("필수, 1 이상의 양의 정수")))
                        .requestFields(
                                fieldWithPath("latitude")
                                        .type(JsonFieldType.NUMBER)
                                        .description("위도(소수)"),
                                fieldWithPath("longitude")
                                        .type(JsonFieldType.NUMBER)
                                        .description("경도(소수)"))
                        .responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("축제 식별자"),
                                fieldWithPath("name")
                                        .type(JsonFieldType.STRING)
                                        .description("축제명")
                                        .optional(),
                                fieldWithPath("festivalName")
                                        .type(JsonFieldType.STRING)
                                        .description("축제명")
                                        .optional(),
                                fieldWithPath("logo")
                                        .type(JsonFieldType.STRING)
                                        .description("로고 파일 URL")
                                        .optional(),
                                fieldWithPath("colors.mainColor")
                                        .type(JsonFieldType.STRING)
                                        .description("메인 색상 (#RGB | #RRGGBB)"),
                                fieldWithPath("colors.backgroundColor")
                                        .type(JsonFieldType.STRING)
                                        .description("배경 색상 (#RGB | #RRGGBB)"),
                                fieldWithPath("colors.subColor")
                                        .type(JsonFieldType.STRING)
                                        .description("서브 색상 (#RGB | #RRGGBB)"),
                                fieldWithPath("colors.fontColor")
                                        .type(JsonFieldType.STRING)
                                        .description("폰트 색상 (#RGB | #RRGGBB)"),
                                fieldWithPath("latitude")
                                        .type(JsonFieldType.NUMBER)
                                        .description("위도(소수)"),
                                fieldWithPath("longitude")
                                        .type(JsonFieldType.NUMBER)
                                        .description("경도(소수)"))
                        .build()));
    }

    public static RestDocumentationResultHandler getFestival() {
        return document(
                "festival 단일 조회",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("축제 상세 조회")
                        .description("축제 ID로 상세 정보를 조회합니다.")
                        .pathParameters(parameterWithName("id")
                                .description("축제 id")
                                .attributes(key("constraints").value("필수, 1 이상의 양의 정수")))
                        .responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("축제 식별자"),
                                fieldWithPath("name")
                                        .type(JsonFieldType.STRING)
                                        .description("축제명")
                                        .optional(),
                                fieldWithPath("festivalName")
                                        .type(JsonFieldType.STRING)
                                        .description("축제명")
                                        .optional(),
                                fieldWithPath("logo").type(JsonFieldType.STRING).description("로고 파일 URL"),
                                fieldWithPath("colors.mainColor")
                                        .type(JsonFieldType.STRING)
                                        .description("메인 색상 (#RGB | #RRGGBB)"),
                                fieldWithPath("colors.backgroundColor")
                                        .type(JsonFieldType.STRING)
                                        .description("배경 색상 (#RGB | #RRGGBB)"),
                                fieldWithPath("colors.subColor")
                                        .type(JsonFieldType.STRING)
                                        .description("서브 색상 (#RGB | #RRGGBB)"),
                                fieldWithPath("colors.fontColor")
                                        .type(JsonFieldType.STRING)
                                        .description("폰트 색상 (#RGB | #RRGGBB)"),
                                fieldWithPath("latitude")
                                        .type(JsonFieldType.NUMBER)
                                        .description("위도(소수)"),
                                fieldWithPath("longitude")
                                        .type(JsonFieldType.NUMBER)
                                        .description("경도(소수)"))
                        .build()));
    }

    /** 리스트 조회 (GET /festivals) - Response: FestivalListDto[] */
    public static RestDocumentationResultHandler getFestivals() {
        return document(
                "festival 리스트 조회",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("축제 목록 조회")
                        .description("전체 축제 목록을 조회합니다.")
                        .responseFields(
                                fieldWithPath("[]").description("축제 목록"),
                                fieldWithPath("[].id")
                                        .type(JsonFieldType.NUMBER)
                                        .description("축제 식별자"),
                                fieldWithPath("[].festivalName")
                                        .type(JsonFieldType.STRING)
                                        .description("축제명"),
                                fieldWithPath("[].subAddress")
                                        .type(JsonFieldType.STRING)
                                        .description("서브 도메인"),
                                fieldWithPath("[].latitude")
                                        .type(JsonFieldType.NUMBER)
                                        .description("위도(소수)"),
                                fieldWithPath("[].longitude")
                                        .type(JsonFieldType.NUMBER)
                                        .description("경도(소수)"))
                        .build()));
    }

    /** 공통 에러 스니펫 */
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
