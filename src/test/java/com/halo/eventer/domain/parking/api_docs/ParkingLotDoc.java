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

public class ParkingLotDoc {

    private static final String TAG = "주차장";

    public static RestDocumentationResultHandler create() {
        return document(
                "parking-lot 생성",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("주차장 생성")
                        .description("특정 주차구역(parkingZoneId)에 주차장을 생성합니다. 주소같은 경우 네이버 API의 변수들 중 필요한 부분만 저장합니다.")
                        .pathParameters(parameterWithName("parkingZoneId")
                                .description("주차구역 id")
                                .attributes(key("validationConstraints").value(List.of(ApiConstraint.JavaxMin2(1)))))
                        .requestSchema(Schema.schema("ParkingLotReqDto"))
                        .requestFields(
                                fieldWithPath("name")
                                        .type(JsonFieldType.STRING)
                                        .description("주차면 이름 (1~50자)")
                                        .attributes(key("validationConstraints")
                                                .value(List.of(
                                                        ApiConstraint.JavaxNotNull(), ApiConstraint.JavaxSize(1, 50)))),
                                fieldWithPath("sido")
                                        .type(JsonFieldType.STRING)
                                        .description("시/도")
                                        .optional(),
                                fieldWithPath("sigungu")
                                        .type(JsonFieldType.STRING)
                                        .description("시/군/구")
                                        .optional(),
                                fieldWithPath("dongmyun")
                                        .type(JsonFieldType.STRING)
                                        .description("읍/면/동")
                                        .optional(),
                                fieldWithPath("roadName")
                                        .type(JsonFieldType.STRING)
                                        .description("도로명")
                                        .optional(),
                                fieldWithPath("roadNumber")
                                        .type(JsonFieldType.STRING)
                                        .description("건물번호")
                                        .optional(),
                                fieldWithPath("buildingName")
                                        .type(JsonFieldType.STRING)
                                        .description("상세주소/건물명")
                                        .optional(),
                                fieldWithPath("latitude")
                                        .type(JsonFieldType.NUMBER)
                                        .description("위도")
                                        .attributes(key("validationConstraints")
                                                .value(List.of(ApiConstraint.JavaxNotNull()))),
                                fieldWithPath("longitude")
                                        .type(JsonFieldType.NUMBER)
                                        .description("경도")
                                        .attributes(key("validationConstraints")
                                                .value(List.of(ApiConstraint.JavaxNotNull()))))
                        .build()));
    }

    public static RestDocumentationResultHandler get() {
        return document(
                "parking-lot 단일 조회",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("주차장 단일 조회")
                        .description("주차장 id로 상세 정보를 조회합니다. 백오피스에서 수정할 때 사용하는 용도입니다.")
                        .responseSchema(Schema.schema("AdminParkingLotResDto"))
                        .pathParameters(parameterWithName("id")
                                .description("주차장 id")
                                .attributes(key("validationConstraints").value(List.of(ApiConstraint.JavaxMin2(1)))))
                        .responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("주차장 id"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("주차면 이름"),
                                fieldWithPath("sido").type(JsonFieldType.STRING).description("시/도"),
                                fieldWithPath("sigungu")
                                        .type(JsonFieldType.STRING)
                                        .description("시/군/구"),
                                fieldWithPath("dongmyun")
                                        .type(JsonFieldType.STRING)
                                        .description("읍/면/동"),
                                fieldWithPath("roadName")
                                        .type(JsonFieldType.STRING)
                                        .description("도로명"),
                                fieldWithPath("roadNumber")
                                        .type(JsonFieldType.STRING)
                                        .description("건물번호"),
                                fieldWithPath("buildingName")
                                        .type(JsonFieldType.STRING)
                                        .description("상세주소/건물명"),
                                fieldWithPath("latitude")
                                        .type(JsonFieldType.NUMBER)
                                        .description("위도"),
                                fieldWithPath("longitude")
                                        .type(JsonFieldType.NUMBER)
                                        .description("경도"))
                        .build()));
    }

    public static RestDocumentationResultHandler changeVisibility() {
        return document(
                "parking-lot 노출 순서 변경",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("주차장 노출 순서 변경")
                        .description("주차장의 노출 여부를 변경합니다.")
                        .requestSchema(Schema.schema("VisibilityChangeReqDto"))
                        .pathParameters(parameterWithName("id")
                                .description("주차장 id")
                                .attributes(key("validationConstraints").value(List.of(ApiConstraint.JavaxMin2(1)))))
                        .requestFields(fieldWithPath("visible")
                                .type(JsonFieldType.BOOLEAN)
                                .description("노출 여부")
                                .attributes(key("validationConstraints").value(List.of(ApiConstraint.JavaxNotNull()))))
                        .build()));
    }

    public static RestDocumentationResultHandler updateContent() {
        return document(
                "parking-lot 내용 수정",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("주차장 내용 수정")
                        .description("주차장 정보를 수정합니다. visible 필드는 포함되지 않습니다.")
                        .requestSchema(Schema.schema("ParkingLotReqDto"))
                        .pathParameters(parameterWithName("id")
                                .description("주차장 id")
                                .attributes(key("validationConstraints").value(List.of(ApiConstraint.JavaxMin2(1)))))
                        .requestFields(
                                fieldWithPath("name")
                                        .type(JsonFieldType.STRING)
                                        .description("주차장 이름 (1~50자)")
                                        .attributes(key("validationConstraints")
                                                .value(List.of(
                                                        ApiConstraint.JavaxNotNull(), ApiConstraint.JavaxSize(1, 50)))),
                                fieldWithPath("sido")
                                        .type(JsonFieldType.STRING)
                                        .description("시/도")
                                        .optional(),
                                fieldWithPath("sigungu")
                                        .type(JsonFieldType.STRING)
                                        .description("시/군/구")
                                        .optional(),
                                fieldWithPath("dongmyun")
                                        .type(JsonFieldType.STRING)
                                        .description("읍/면/동")
                                        .optional(),
                                fieldWithPath("roadName")
                                        .type(JsonFieldType.STRING)
                                        .description("도로명")
                                        .optional(),
                                fieldWithPath("roadNumber")
                                        .type(JsonFieldType.STRING)
                                        .description("건물번호")
                                        .optional(),
                                fieldWithPath("buildingName")
                                        .type(JsonFieldType.STRING)
                                        .description("상세주소/건물명")
                                        .optional(),
                                fieldWithPath("latitude")
                                        .type(JsonFieldType.NUMBER)
                                        .description("위도")
                                        .attributes(key("validationConstraints")
                                                .value(List.of(ApiConstraint.JavaxNotNull()))),
                                fieldWithPath("longitude")
                                        .type(JsonFieldType.NUMBER)
                                        .description("경도")
                                        .attributes(key("validationConstraints")
                                                .value(List.of(ApiConstraint.JavaxNotNull()))))
                        .build()));
    }

    public static RestDocumentationResultHandler changeCongestionLevel() {
        return document(
                "parking-lot 혼잡도 변경",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("주차장 혼잡도 변경")
                        .description("주차장의 혼잡도 레벨을 변경합니다. (예: LOW/MEDIUM/HIGH/FULL/CLOSED)")
                        .requestSchema(Schema.schema("CongestionLevelReqDto"))
                        .pathParameters(parameterWithName("id")
                                .description("주차장 id")
                                .attributes(key("validationConstraints").value(List.of(ApiConstraint.JavaxMin2(1)))))
                        .requestFields(fieldWithPath("congestionLevel")
                                .type(JsonFieldType.STRING)
                                .description("혼잡도 레벨 (LOW/MEDIUM/HIGH/FULL/CLOSED)")
                                .attributes(key("validationConstraints").value(List.of(ApiConstraint.JavaxNotNull()))))
                        .build()));
    }

    public static RestDocumentationResultHandler delete() {
        return document(
                "parking-lot 삭제",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("주차장 삭제")
                        .description("주차장 id로 주차장을 삭제합니다.")
                        .pathParameters(parameterWithName("id")
                                .description("주차장 id")
                                .attributes(key("validationConstraints").value(List.of(ApiConstraint.JavaxMin2(1)))))
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
