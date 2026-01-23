package com.halo.eventer.domain.parking.api_docs;

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

public class ParkingZoneDoc {

    private static final String TAG = "주차구역";

    public static RestDocumentationResultHandler create() {
        return document(
                "parking-zone 생성",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("주차구역 생성")
                        .description(
                                "특정 주차관리(parkingManagementId)에 주차구역을 생성합니다. 피그마 상에 노출여부도 생성할 때 결정할 수 있어 주차장 지도와는 다르게 visible이 추가되었습니다.")
                        .pathParameters(parameterWithName("parkingManagementId")
                                .description("주차관리 id")
                                .attributes(key("validationConstraints").value(List.of(ApiConstraint.JavaxMin2(1)))))
                        .requestSchema(Schema.schema("ParkingZoneReqDto"))
                        .requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("주차구역 이름 (1~50자)"),
                                fieldWithPath("visible")
                                        .type(JsonFieldType.BOOLEAN)
                                        .description("노출 여부 (NotNull)"))
                        .build()));
    }

    public static RestDocumentationResultHandler getAdminList() {
        return document(
                "parking-zone 리스트 조회(ADMIN)",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("주차구역 리스트 조회(관리자)")
                        .description(
                                "주차관리 id 기준으로 전체 주차구역 목록을 조회합니다. 구역 및 주차장 리스트 페이지, 주차장 혼잡도 페이지에서 모두 이 API 하나만 사용 (관리자용)")
                        .responseSchema(Schema.schema("ParkingZoneResDtoList"))
                        .pathParameters(parameterWithName("parkingManagementId")
                                .description("주차관리 id")
                                .attributes(key("validationConstraints").value(List.of(ApiConstraint.JavaxMin2(1)))))
                        .responseFields(
                                fieldWithPath("[]").description("주차구역 목록"),
                                fieldWithPath("[].id")
                                        .type(JsonFieldType.NUMBER)
                                        .description("주차구역 id"),
                                fieldWithPath("[].name")
                                        .type(JsonFieldType.STRING)
                                        .description("주차구역 이름"),
                                fieldWithPath("[].visible")
                                        .type(JsonFieldType.BOOLEAN)
                                        .description("노출 여부"),
                                fieldWithPath("[].parkingLots[]")
                                        .type(JsonFieldType.ARRAY)
                                        .description("주차면 요약 목록"),
                                fieldWithPath("[].parkingLots[].id")
                                        .type(JsonFieldType.NUMBER)
                                        .description("주차면 id"),
                                fieldWithPath("[].parkingLots[].name")
                                        .type(JsonFieldType.STRING)
                                        .description("주차면 이름"),
                                fieldWithPath("[].parkingLots[].congestionLevel")
                                        .type(JsonFieldType.STRING)
                                        .description("혼잡도 레벨"),
                                fieldWithPath("[].parkingLots[].visible")
                                        .type(JsonFieldType.BOOLEAN)
                                        .description("노출 여부"),
                                fieldWithPath("[].parkingLots[].copyAddress")
                                        .type(JsonFieldType.STRING)
                                        .description("복사용 주소"),
                                fieldWithPath("[].parkingLots[].displayAddress")
                                        .type(JsonFieldType.STRING)
                                        .description("노출용 주소"),
                                fieldWithPath("[].parkingLots[].latitude")
                                        .type(JsonFieldType.NUMBER)
                                        .description("주차장 위도"),
                                fieldWithPath("[].parkingLots[].longitude")
                                        .type(JsonFieldType.NUMBER)
                                        .description("주차장 경도"))
                        .build()));
    }

    public static RestDocumentationResultHandler getUserList() {
        return document(
                "parking-zone 리스트 조회(USER 공개)",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("주차구역 공개 리스트 조회(사용자)")
                        .description(
                                "주차관리 id 기준으로 공개 상태의 주차구역 목록을 조회합니다. (사용자용) 주차 구역 visible = false일 경우 그 하위 주차장 모두 조회 불가")
                        .responseSchema(Schema.schema("ParkingZoneResDtoList"))
                        .pathParameters(parameterWithName("parkingManagementId")
                                .description("주차관리 id")
                                .attributes(key("validationConstraints").value(List.of(ApiConstraint.JavaxMin2(1)))))
                        .responseFields(
                                fieldWithPath("[]").description("공개 주차구역 목록"),
                                fieldWithPath("[].id")
                                        .type(JsonFieldType.NUMBER)
                                        .description("주차구역 id"),
                                fieldWithPath("[].name")
                                        .type(JsonFieldType.STRING)
                                        .description("주차구역 이름"),
                                fieldWithPath("[].visible")
                                        .type(JsonFieldType.BOOLEAN)
                                        .description("노출 여부"),
                                fieldWithPath("[].parkingLots[]")
                                        .type(JsonFieldType.ARRAY)
                                        .description("주차면 요약 목록"),
                                fieldWithPath("[].parkingLots[].id")
                                        .type(JsonFieldType.NUMBER)
                                        .description("주차면 id"),
                                fieldWithPath("[].parkingLots[].name")
                                        .type(JsonFieldType.STRING)
                                        .description("주차면 이름"),
                                fieldWithPath("[].parkingLots[].congestionLevel")
                                        .type(JsonFieldType.STRING)
                                        .description("혼잡도 레벨"),
                                fieldWithPath("[].parkingLots[].visible")
                                        .type(JsonFieldType.BOOLEAN)
                                        .description("노출 여부"),
                                fieldWithPath("[].parkingLots[].copyAddress")
                                        .type(JsonFieldType.STRING)
                                        .description("복사용 주소"),
                                fieldWithPath("[].parkingLots[].displayAddress")
                                        .type(JsonFieldType.STRING)
                                        .description("노출용 주소"),
                                fieldWithPath("[].parkingLots[].latitude")
                                        .type(JsonFieldType.NUMBER)
                                        .description("주차장 위도"),
                                fieldWithPath("[].parkingLots[].longitude")
                                        .type(JsonFieldType.NUMBER)
                                        .description("주차장 경도"))
                        .build()));
    }

    public static RestDocumentationResultHandler changeVisibility() {
        return document(
                "parking-zone 노출 여부 변경",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("주차구역 노출 여부 변경")
                        .description("주차구역의 노출 여부를 변경합니다.")
                        .requestSchema(Schema.schema("VisibilityChangeReqDto"))
                        .pathParameters(parameterWithName("id")
                                .description("주차구역 id")
                                .attributes(key("validationConstraints").value(List.of(ApiConstraint.JavaxMin2(1)))))
                        .requestFields(fieldWithPath("visible")
                                .type(JsonFieldType.BOOLEAN)
                                .description("노출 여부")
                                .attributes(key("validationConstraints").value(List.of(ApiConstraint.JavaxNotNull()))))
                        .build()));
    }

    /**
     * PATCH /api/v2/admin/parking-zones/{id}/name
     * 이름 변경
     */
    public static RestDocumentationResultHandler rename() {
        return document(
                "parking-zone 이름 변경",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("주차구역 이름 변경")
                        .description("주차구역 이름을 변경합니다.")
                        .requestSchema(Schema.schema("NameUpdateReqDto"))
                        .pathParameters(parameterWithName("id")
                                .description("주차구역 id")
                                .attributes(key("validationConstraints").value(List.of(ApiConstraint.JavaxMin2(1)))))
                        .requestFields(fieldWithPath("name")
                                .type(JsonFieldType.STRING)
                                .description("변경할 이름 (1~50자)")
                                .attributes(key("validationConstraints")
                                        .value(List.of(ApiConstraint.JavaxNotEmpty(), ApiConstraint.JavaxSize(1, 50)))))
                        .build()));
    }

    /**
     * PATCH /api/v2/admin/parking-managements/{parkingManagementId}/parking-zones/display-order
     * 표시 순서 변경
     */
    public static RestDocumentationResultHandler changeDisplayOrder() {
        return document(
                "parking-zone 표시순서 변경",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("주차구역 표시 순서 변경")
                        .description("주차구역의 표시 순서를 일괄 변경합니다. 주차장 지도 순서 변경 로직과 동일하게 동작합니다.")
                        .requestSchema(Schema.schema("DisplayOrderChangeReqDto"))
                        .pathParameters(parameterWithName("parkingManagementId")
                                .description("주차관리 id")
                                .attributes(key("validationConstraints").value(List.of(ApiConstraint.JavaxMin2(1)))))
                        .requestFields(fieldWithPath("ids[]")
                                .type(JsonFieldType.ARRAY)
                                .description("표시 순서에 맞춘 주차구역 id 목록 (선두가 가장 먼저 표시)")
                                .attributes(key("validationConstraints").value(List.of(ApiConstraint.JavaxNotEmpty()))))
                        .build()));
    }

    public static RestDocumentationResultHandler delete() {
        return document(
                "parking-zone 삭제",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("주차구역 삭제")
                        .description("주차구역 id로 주차구역을 삭제합니다.")
                        .pathParameters(
                                parameterWithName("id")
                                        .description("주차구역 id")
                                        .attributes(key("validationConstraints")
                                                .value(List.of(ApiConstraint.JavaxMin2(1)))),
                                parameterWithName("parkingManagementId")
                                        .description("주차 관리 시스템 id")
                                        .attributes(key("validationConstraints")
                                                .value(List.of(ApiConstraint.JavaxMin2(1)))))
                        .build()));
    }

    /**
     * 공통 에러 스니펫
     */
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
