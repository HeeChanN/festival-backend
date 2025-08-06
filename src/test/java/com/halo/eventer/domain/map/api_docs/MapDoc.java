package com.halo.eventer.domain.map.api_docs;

import java.util.List;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.halo.eventer.global.constants.ApiConstraint;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.snippet.Attributes.key;

public class MapDoc {
    private static final String TAG = "지도";

    public static RestDocumentationResultHandler createMap() {
        return document(
                "map 생성",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("맵(부스) 생성")
                        .description("특정 지도 카테고리(mapCategoryId)에 맵(부스)을 생성합니다.")
                        .pathParameters(parameterWithName("mapCategoryId")
                                .description("지도 카테고리 id")
                                .attributes(key("validationConstraints").value(List.of(ApiConstraint.JavaxMin2(1)))))
                        .requestFields(
                                fieldWithPath("name")
                                        .type(JsonFieldType.STRING)
                                        .description("이름 (1~20자)")
                                        .attributes(key("validationConstraints")
                                                .value(List.of(
                                                        ApiConstraint.JavaxNotNull(), ApiConstraint.JavaxSize(1, 20)))),
                                fieldWithPath("summary")
                                        .type(JsonFieldType.STRING)
                                        .description("요약")
                                        .optional(),
                                fieldWithPath("content")
                                        .type(JsonFieldType.STRING)
                                        .description("상세 내용")
                                        .optional(),
                                fieldWithPath("thumbnail")
                                        .type(JsonFieldType.STRING)
                                        .description("썸네일 URL/식별자")
                                        .optional(),
                                fieldWithPath("icon")
                                        .type(JsonFieldType.STRING)
                                        .description("아이콘 URL/식별자")
                                        .optional(),
                                fieldWithPath("operationInfo")
                                        .type(JsonFieldType.OBJECT)
                                        .description("운영 정보 (객체)")
                                        .attributes(key("validationConstraints")
                                                .value(List.of(ApiConstraint.JavaxNotNull()))),
                                fieldWithPath("operationInfo.hours")
                                        .type(JsonFieldType.STRING)
                                        .description("영업 시간(표시용 문자열)")
                                        .optional(),
                                fieldWithPath("operationInfo.type")
                                        .type(JsonFieldType.STRING)
                                        .description("운영 타입 (예: morning/afternoon 등)")
                                        .attributes(key("validationConstraints")
                                                .value(List.of(ApiConstraint.JavaxNotNull()))),
                                fieldWithPath("locationInfo")
                                        .type(JsonFieldType.OBJECT)
                                        .description("위치 정보 (객체)")
                                        .attributes(key("validationConstraints")
                                                .value(List.of(ApiConstraint.JavaxNotNull()))),
                                fieldWithPath("locationInfo.address")
                                        .type(JsonFieldType.STRING)
                                        .description("주소(장소 정보)")
                                        .optional(),
                                fieldWithPath("locationInfo.latitude")
                                        .type(JsonFieldType.NUMBER)
                                        .description("위도"),
                                fieldWithPath("locationInfo.longitude")
                                        .type(JsonFieldType.NUMBER)
                                        .description("경도"),
                                fieldWithPath("buttonInfo")
                                        .type(JsonFieldType.OBJECT)
                                        .description("버튼 정보 (객체)")
                                        .attributes(key("validationConstraints")
                                                .value(List.of(ApiConstraint.JavaxNotNull()))),
                                fieldWithPath("buttonInfo.name")
                                        .type(JsonFieldType.STRING)
                                        .description("버튼 이름")
                                        .optional(),
                                fieldWithPath("buttonInfo.url")
                                        .type(JsonFieldType.STRING)
                                        .description("연결 주소(URL)")
                                        .optional(),
                                fieldWithPath("buttonInfo.image")
                                        .type(JsonFieldType.STRING)
                                        .description("버튼 이미지 URL")
                                        .optional(),
                                fieldWithPath("durationIdsToAdd[]")
                                        .type(JsonFieldType.ARRAY)
                                        .description("연결할 기간 ID 목록")
                                        .optional())
                        .responseFields(
                                fieldWithPath("mapId")
                                        .type(JsonFieldType.NUMBER)
                                        .description("맵 id"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("summary")
                                        .type(JsonFieldType.STRING)
                                        .description("요약")
                                        .optional(),
                                fieldWithPath("content")
                                        .type(JsonFieldType.STRING)
                                        .description("상세 내용")
                                        .optional(),
                                fieldWithPath("thumbnail")
                                        .type(JsonFieldType.STRING)
                                        .description("썸네일 URL/식별자")
                                        .optional(),
                                fieldWithPath("icon")
                                        .type(JsonFieldType.STRING)
                                        .description("아이콘 URL/식별자")
                                        .optional(),
                                fieldWithPath("locationInfo")
                                        .type(JsonFieldType.OBJECT)
                                        .description("위치 정보 (객체)"),
                                fieldWithPath("locationInfo.address")
                                        .type(JsonFieldType.STRING)
                                        .description("주소(장소 정보)")
                                        .optional(),
                                fieldWithPath("locationInfo.latitude")
                                        .type(JsonFieldType.NUMBER)
                                        .description("위도"),
                                fieldWithPath("locationInfo.longitude")
                                        .type(JsonFieldType.NUMBER)
                                        .description("경도"),
                                fieldWithPath("operationInfo")
                                        .type(JsonFieldType.OBJECT)
                                        .description("운영 정보 (객체)"),
                                fieldWithPath("operationInfo.hours")
                                        .type(JsonFieldType.STRING)
                                        .description("영업 시간(표시용 문자열)")
                                        .optional(),
                                fieldWithPath("operationInfo.type")
                                        .type(JsonFieldType.STRING)
                                        .description("운영 타입 (예: morning/afternoon 등)")
                                        .attributes(key("validationConstraints")
                                                .value(List.of(ApiConstraint.JavaxNotNull()))),
                                fieldWithPath("buttonInfo")
                                        .type(JsonFieldType.OBJECT)
                                        .description("버튼 정보 (객체)"),
                                fieldWithPath("buttonInfo.name")
                                        .type(JsonFieldType.STRING)
                                        .description("버튼 이름")
                                        .optional(),
                                fieldWithPath("buttonInfo.url")
                                        .type(JsonFieldType.STRING)
                                        .description("연결 주소(URL)")
                                        .optional(),
                                fieldWithPath("buttonInfo.image")
                                        .type(JsonFieldType.STRING)
                                        .description("버튼 이미지 URL")
                                        .optional(),
                                fieldWithPath("categoryName")
                                        .type(JsonFieldType.STRING)
                                        .description("소속 카테고리 이름"),
                                fieldWithPath("durations")
                                        .type(JsonFieldType.ARRAY)
                                        .description("연결된 기간 목록(DurationResDto[])"),
                                fieldWithPath("durations[].date")
                                        .type(JsonFieldType.STRING)
                                        .description("기간 날짜 (yyyy-MM-dd)"),
                                fieldWithPath("durations[].durationId")
                                        .type(JsonFieldType.NUMBER)
                                        .description("기간 ID"),
                                fieldWithPath("durations[].dayNumber")
                                        .type(JsonFieldType.NUMBER)
                                        .description("축제 시작 기준 N일차 (1부터)"))
                        .build()));
    }

    public static RestDocumentationResultHandler getMap() {
        return document(
                "map 단일 조회",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("맵(부스) 단일 조회")
                        .description("맵 id로 상세 정보를 조회합니다.")
                        .responseSchema(Schema.schema("MapResDto"))
                        .pathParameters(parameterWithName("mapId")
                                .description("맵 id")
                                .attributes(key("validationConstraints").value(List.of(ApiConstraint.JavaxMin2(1)))))
                        .responseFields(
                                fieldWithPath("mapId")
                                        .type(JsonFieldType.NUMBER)
                                        .description("맵 id"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("summary")
                                        .type(JsonFieldType.STRING)
                                        .description("요약")
                                        .optional(),
                                fieldWithPath("content")
                                        .type(JsonFieldType.STRING)
                                        .description("상세 내용")
                                        .optional(),
                                fieldWithPath("thumbnail")
                                        .type(JsonFieldType.STRING)
                                        .description("썸네일 URL/식별자")
                                        .optional(),
                                fieldWithPath("icon")
                                        .type(JsonFieldType.STRING)
                                        .description("아이콘 URL/식별자")
                                        .optional(),
                                fieldWithPath("locationInfo")
                                        .type(JsonFieldType.OBJECT)
                                        .description("위치 정보 (객체)"),
                                fieldWithPath("locationInfo.address")
                                        .type(JsonFieldType.STRING)
                                        .description("주소(장소 정보)")
                                        .optional(),
                                fieldWithPath("locationInfo.latitude")
                                        .type(JsonFieldType.NUMBER)
                                        .description("위도"),
                                fieldWithPath("locationInfo.longitude")
                                        .type(JsonFieldType.NUMBER)
                                        .description("경도"),
                                fieldWithPath("operationInfo")
                                        .type(JsonFieldType.OBJECT)
                                        .description("운영 정보 (객체)"),
                                fieldWithPath("operationInfo.hours")
                                        .type(JsonFieldType.STRING)
                                        .description("영업 시간(표시용 문자열)")
                                        .optional(),
                                fieldWithPath("operationInfo.type")
                                        .type(JsonFieldType.STRING)
                                        .description("운영 타입 (예: morning/afternoon 등)")
                                        .attributes(key("validationConstraints")
                                                .value(List.of(ApiConstraint.JavaxNotNull()))),
                                fieldWithPath("buttonInfo")
                                        .type(JsonFieldType.OBJECT)
                                        .description("버튼 정보 (객체)"),
                                fieldWithPath("buttonInfo.name")
                                        .type(JsonFieldType.STRING)
                                        .description("버튼 이름")
                                        .optional(),
                                fieldWithPath("buttonInfo.url")
                                        .type(JsonFieldType.STRING)
                                        .description("연결 주소(URL)")
                                        .optional(),
                                fieldWithPath("buttonInfo.image")
                                        .type(JsonFieldType.STRING)
                                        .description("버튼 이미지 URL")
                                        .optional(),
                                fieldWithPath("categoryName")
                                        .type(JsonFieldType.STRING)
                                        .description("소속 카테고리 이름"),
                                fieldWithPath("durations")
                                        .type(JsonFieldType.ARRAY)
                                        .description("연결된 기간 목록(DurationResDto[])"),
                                fieldWithPath("durations[].date")
                                        .type(JsonFieldType.STRING)
                                        .description("기간 날짜 (yyyy-MM-dd)"),
                                fieldWithPath("durations[].durationId")
                                        .type(JsonFieldType.NUMBER)
                                        .description("기간 ID"),
                                fieldWithPath("durations[].dayNumber")
                                        .type(JsonFieldType.NUMBER)
                                        .description("축제 시작 기준 N일차 (1부터)"))
                        .build()));
    }

    public static RestDocumentationResultHandler updateMap() {
        return document(
                "map 수정",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("맵(부스) 수정")
                        .description("맵 id와 지도 카테고리 id 기준으로 맵(부스) 정보를 수정합니다.")
                        .requestSchema(Schema.schema("MapUpdateDto"))
                        .responseSchema(Schema.schema("MapResDto"))
                        .pathParameters(
                                parameterWithName("mapCategoryId")
                                        .description("지도 카테고리 id")
                                        .attributes(key("validationConstraints")
                                                .value(List.of(ApiConstraint.JavaxMin2(1)))),
                                parameterWithName("mapId")
                                        .description("맵 id")
                                        .attributes(key("validationConstraints")
                                                .value(List.of(ApiConstraint.JavaxMin2(1)))))
                        .requestFields(
                                fieldWithPath("name")
                                        .type(JsonFieldType.STRING)
                                        .description("이름 (1~20자)")
                                        .attributes(key("validationConstraints")
                                                .value(List.of(
                                                        ApiConstraint.JavaxNotNull(), ApiConstraint.JavaxSize(1, 20)))),
                                fieldWithPath("summary")
                                        .type(JsonFieldType.STRING)
                                        .description("요약")
                                        .optional(),
                                fieldWithPath("content")
                                        .type(JsonFieldType.STRING)
                                        .description("상세 내용")
                                        .optional(),
                                fieldWithPath("thumbnail")
                                        .type(JsonFieldType.STRING)
                                        .description("썸네일 URL/식별자")
                                        .optional(),
                                fieldWithPath("icon")
                                        .type(JsonFieldType.STRING)
                                        .description("아이콘 URL/식별자")
                                        .optional(),
                                fieldWithPath("operationInfo")
                                        .type(JsonFieldType.OBJECT)
                                        .description("운영 정보 (객체)")
                                        .attributes(key("validationConstraints")
                                                .value(List.of(ApiConstraint.JavaxNotNull()))),
                                fieldWithPath("operationInfo.hours")
                                        .type(JsonFieldType.STRING)
                                        .description("영업 시간(표시용 문자열)")
                                        .optional(),
                                fieldWithPath("operationInfo.type")
                                        .type(JsonFieldType.STRING)
                                        .description("운영 타입 (예: morning/afternoon 등)")
                                        .attributes(key("validationConstraints")
                                                .value(List.of(ApiConstraint.JavaxNotNull()))),
                                fieldWithPath("locationInfo")
                                        .type(JsonFieldType.OBJECT)
                                        .description("위치 정보 (객체)")
                                        .attributes(key("validationConstraints")
                                                .value(List.of(ApiConstraint.JavaxNotNull()))),
                                fieldWithPath("locationInfo.address")
                                        .type(JsonFieldType.STRING)
                                        .description("주소(장소 정보)")
                                        .optional(),
                                fieldWithPath("locationInfo.latitude")
                                        .type(JsonFieldType.NUMBER)
                                        .description("위도"),
                                fieldWithPath("locationInfo.longitude")
                                        .type(JsonFieldType.NUMBER)
                                        .description("경도"),
                                fieldWithPath("buttonInfo")
                                        .type(JsonFieldType.OBJECT)
                                        .description("버튼 정보 (객체)")
                                        .attributes(key("validationConstraints")
                                                .value(List.of(ApiConstraint.JavaxNotNull()))),
                                fieldWithPath("durationBinding")
                                        .type(JsonFieldType.OBJECT)
                                        .description("기간 바인딩 정보"),
                                fieldWithPath("buttonInfo.name")
                                        .type(JsonFieldType.STRING)
                                        .description("버튼 이름")
                                        .optional(),
                                fieldWithPath("buttonInfo.url")
                                        .type(JsonFieldType.STRING)
                                        .description("연결 주소(URL)")
                                        .optional(),
                                fieldWithPath("buttonInfo.image")
                                        .type(JsonFieldType.STRING)
                                        .description("버튼 이미지 URL")
                                        .optional(),
                                fieldWithPath("durationBinding.idsToAdd[]")
                                        .type(JsonFieldType.ARRAY)
                                        .description("추가할 기간 ID 목록"),
                                fieldWithPath("durationBinding.idsToRemove[]")
                                        .type(JsonFieldType.ARRAY)
                                        .description("제거할 기간 ID 목록"))
                        .responseFields(
                                fieldWithPath("mapId")
                                        .type(JsonFieldType.NUMBER)
                                        .description("맵 id"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("summary")
                                        .type(JsonFieldType.STRING)
                                        .description("요약")
                                        .optional(),
                                fieldWithPath("content")
                                        .type(JsonFieldType.STRING)
                                        .description("상세 내용")
                                        .optional(),
                                fieldWithPath("thumbnail")
                                        .type(JsonFieldType.STRING)
                                        .description("썸네일 URL/식별자")
                                        .optional(),
                                fieldWithPath("icon")
                                        .type(JsonFieldType.STRING)
                                        .description("아이콘 URL/식별자")
                                        .optional(),
                                fieldWithPath("locationInfo")
                                        .type(JsonFieldType.OBJECT)
                                        .description("위치 정보 (객체)"),
                                fieldWithPath("locationInfo.address")
                                        .type(JsonFieldType.STRING)
                                        .description("주소(장소 정보)")
                                        .optional(),
                                fieldWithPath("locationInfo.latitude")
                                        .type(JsonFieldType.NUMBER)
                                        .description("위도"),
                                fieldWithPath("locationInfo.longitude")
                                        .type(JsonFieldType.NUMBER)
                                        .description("경도"),
                                fieldWithPath("operationInfo")
                                        .type(JsonFieldType.OBJECT)
                                        .description("운영 정보 (객체)"),
                                fieldWithPath("operationInfo.hours")
                                        .type(JsonFieldType.STRING)
                                        .description("영업 시간(표시용 문자열)")
                                        .optional(),
                                fieldWithPath("operationInfo.type")
                                        .type(JsonFieldType.STRING)
                                        .description("운영 타입 (예: morning/afternoon 등)")
                                        .attributes(key("validationConstraints")
                                                .value(List.of(ApiConstraint.JavaxNotNull()))),
                                fieldWithPath("buttonInfo")
                                        .type(JsonFieldType.OBJECT)
                                        .description("버튼 정보 (객체)"),
                                fieldWithPath("buttonInfo.name")
                                        .type(JsonFieldType.STRING)
                                        .description("버튼 이름")
                                        .optional(),
                                fieldWithPath("buttonInfo.url")
                                        .type(JsonFieldType.STRING)
                                        .description("연결 주소(URL)")
                                        .optional(),
                                fieldWithPath("buttonInfo.image")
                                        .type(JsonFieldType.STRING)
                                        .description("버튼 이미지 URL")
                                        .optional(),
                                fieldWithPath("categoryName")
                                        .type(JsonFieldType.STRING)
                                        .description("소속 카테고리 이름"),
                                fieldWithPath("durations[]")
                                        .type(JsonFieldType.ARRAY)
                                        .description("연결된 기간 목록"),
                                fieldWithPath("durations[].date")
                                        .type(JsonFieldType.STRING)
                                        .description("기간 날짜 (yyyy-MM-dd)"),
                                fieldWithPath("durations[].durationId")
                                        .type(JsonFieldType.NUMBER)
                                        .description("기간 ID"),
                                fieldWithPath("durations[].dayNumber")
                                        .type(JsonFieldType.NUMBER)
                                        .description("축제 시작 기준 N일차 (1부터)"))
                        .build()));
    }

    public static RestDocumentationResultHandler getMaps() {
        return document(
                "map 리스트 조회",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("맵(부스) 리스트 조회")
                        .description("지도 카테고리 id 기준으로 맵(부스) 목록을 조회합니다.")
                        .responseSchema(Schema.schema("MapItemDtoList"))
                        .pathParameters(parameterWithName("mapCategoryId")
                                .description("지도 카테고리 id")
                                .attributes(key("validationConstraints").value(List.of(ApiConstraint.JavaxMin2(1)))))
                        .responseFields(
                                fieldWithPath("[]").description("맵(부스) 항목 목록"),
                                fieldWithPath("[].id")
                                        .type(JsonFieldType.NUMBER)
                                        .description("맵 id"),
                                fieldWithPath("[].name")
                                        .type(JsonFieldType.STRING)
                                        .description("이름"),
                                fieldWithPath("[].operationTime")
                                        .type(JsonFieldType.STRING)
                                        .description("운영시간 정보(표시용 문자열)"),
                                fieldWithPath("[].locationInfo.address")
                                        .type(JsonFieldType.STRING)
                                        .description("주소"),
                                fieldWithPath("[].locationInfo.latitude")
                                        .type(JsonFieldType.NUMBER)
                                        .description("위도"),
                                fieldWithPath("[].locationInfo.longitude")
                                        .type(JsonFieldType.NUMBER)
                                        .description("경도"),
                                fieldWithPath("[].categoryName")
                                        .type(JsonFieldType.STRING)
                                        .description("카테고리명"),
                                fieldWithPath("[].icon")
                                        .type(JsonFieldType.STRING)
                                        .description("아이콘 URL/식별자"),
                                fieldWithPath("[].durationResDto[]")
                                        .type(JsonFieldType.ARRAY)
                                        .description("연결된 기간 목록"),
                                fieldWithPath("[].durationResDto[].date")
                                        .type(JsonFieldType.STRING)
                                        .description("기간 날짜 (yyyy-MM-dd)"),
                                fieldWithPath("[].durationResDto[].durationId")
                                        .type(JsonFieldType.NUMBER)
                                        .description("기간 ID"),
                                fieldWithPath("[].durationResDto[].dayNumber")
                                        .type(JsonFieldType.NUMBER)
                                        .description("축제 시작 기준 N일차 (1부터)"))
                        .build()));
    }

    public static RestDocumentationResultHandler deleteMap() {
        return document(
                "map 삭제",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("맵(부스) 삭제")
                        .description("맵 id로 맵(부스)을 삭제합니다.")
                        .pathParameters(parameterWithName("mapId")
                                .description("맵 id")
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
