package com.halo.eventer.domain.parking.api_docs;

import java.util.List;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.halo.eventer.global.constants.ApiConstraint;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.snippet.Attributes.key;

public class ParkingManagementDoc {

    private static final String TAG = "주차 관리";

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

    // ========== ADMIN ==========
    public static RestDocumentationResultHandler create() {
        return document(
                "parking-management 생성",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("주차 관리 생성 (ADMIN)")
                        .description(
                                "축제 ID에 해당하는 주차 관리 페이지를 생성합니다. visible 필드의 경우 (웹 사이트 사용 여부 = 소개 컴포넌트 사용여부)로 결정해서 연동하면 됩니다. 이유는 2개가 굳이 필요없어요. 결국 소개 컴포넌트를 끄는 행위랑 웹사이트 표시 여부를 바꾸는 행위는 같습니다.")
                        .requestSchema(Schema.schema("ParkingManagementReqDto"))
                        .pathParameters(parameterWithName("festivalId")
                                .description("축제 ID")
                                .attributes(key("validationConstraints").value(List.of(ApiConstraint.JavaxMin2(1)))))
                        .requestHeaders(headerWithName("Authorization").description("Bearer {JWT}"))
                        .requestFields(
                                fieldWithPath("headerName")
                                        .type(JsonFieldType.STRING)
                                        .description("헤더명"),
                                fieldWithPath("parkingInfoType")
                                        .type(JsonFieldType.STRING)
                                        .description("표시 타입 (BASIC|BUTTON|SIMPLE)")
                                        .attributes(key("validationConstraints")
                                                .value(List.of(ApiConstraint.JavaxPattern("BASIC|BUTTON|SIMPLE")))),
                                fieldWithPath("title")
                                        .type(JsonFieldType.STRING)
                                        .description("타이틀")
                                        .optional(),
                                fieldWithPath("description")
                                        .type(JsonFieldType.STRING)
                                        .description("설명")
                                        .optional(),
                                fieldWithPath("buttonName")
                                        .type(JsonFieldType.STRING)
                                        .description("버튼명")
                                        .optional(),
                                fieldWithPath("buttonTargetUrl")
                                        .type(JsonFieldType.STRING)
                                        .description("버튼 링크 URL")
                                        .optional(),
                                fieldWithPath("backgroundImage")
                                        .type(JsonFieldType.STRING)
                                        .description("배경 이미지 URL")
                                        .optional(),
                                fieldWithPath("visible")
                                        .type(JsonFieldType.BOOLEAN)
                                        .description("노출 여부 (웹 사이트 사용 여부 = 소개 컴포넌트 사용여부)")
                                        .optional())
                        .build()));
    }

    public static RestDocumentationResultHandler update() {
        return document(
                "parking-management 수정",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("주차 관리 수정 (ADMIN)")
                        .description("주차 관리 정보를 수정합니다.")
                        .requestSchema(Schema.schema("ParkingManagementReqDto"))
                        .pathParameters(parameterWithName("id")
                                .description("주차 관리 ID")
                                .attributes(key("validationConstraints").value(List.of(ApiConstraint.JavaxMin2(1)))))
                        .requestHeaders(headerWithName("Authorization").description("Bearer {JWT}"))
                        .requestFields(
                                fieldWithPath("headerName")
                                        .type(JsonFieldType.STRING)
                                        .description("헤더명")
                                        .optional(),
                                fieldWithPath("parkingInfoType")
                                        .type(JsonFieldType.STRING)
                                        .description("표시 타입 (BASIC|BUTTON|SIMPLE)"),
                                fieldWithPath("title")
                                        .type(JsonFieldType.STRING)
                                        .description("타이틀")
                                        .optional(),
                                fieldWithPath("description")
                                        .type(JsonFieldType.STRING)
                                        .description("설명")
                                        .optional(),
                                fieldWithPath("buttonName")
                                        .type(JsonFieldType.STRING)
                                        .description("버튼명")
                                        .optional(),
                                fieldWithPath("buttonTargetUrl")
                                        .type(JsonFieldType.STRING)
                                        .description("버튼 링크 URL")
                                        .optional(),
                                fieldWithPath("backgroundImage")
                                        .type(JsonFieldType.STRING)
                                        .description("배경 이미지 URL")
                                        .optional(),
                                fieldWithPath("visible")
                                        .type(JsonFieldType.BOOLEAN)
                                        .description("노출 여부"))
                        .build()));
    }

    public static RestDocumentationResultHandler updateSubPageHeaderName() {
        return document(
                "parking-management 서브페이지-헤더명 수정",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("서브페이지 헤더명 수정 (ADMIN)")
                        .description("서브페이지 상단에 표시할 헤더명을 수정합니다.")
                        .requestSchema(Schema.schema("ParkingSubPageReqDto"))
                        .pathParameters(parameterWithName("id")
                                .description("주차 관리 ID")
                                .attributes(key("validationConstraints").value(List.of(ApiConstraint.JavaxMin2(1)))))
                        .requestHeaders(headerWithName("Authorization").description("Bearer {JWT}"))
                        .requestFields(fieldWithPath("subPageHeaderName")
                                .type(JsonFieldType.STRING)
                                .description("서브페이지 헤더명")
                                .attributes(key("constraints").value("필수, NotNull")))
                        .build()));
    }

    public static RestDocumentationResultHandler createParkingMapImage() {
        return document(
                "parking-management 주차장 지도 이미지 등록",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("주차장 지도 이미지 등록 (ADMIN)")
                        .description("주차장 지도 이미지를 1건 등록합니다. (최대 이미지 20개 등록 가능)")
                        .requestSchema(Schema.schema("FileDto"))
                        .pathParameters(parameterWithName("id")
                                .description("주차 관리 ID")
                                .attributes(key("validationConstraints").value(List.of(ApiConstraint.JavaxMin2(1)))))
                        .requestHeaders(headerWithName("Authorization").description("Bearer {JWT}"))
                        .requestFields(fieldWithPath("url")
                                .type(JsonFieldType.STRING)
                                .description("이미지 URL (대체 필드)")
                                .optional())
                        .build()));
    }

    public static RestDocumentationResultHandler updateParkingMapImageDisplayOrder() {
        return document(
                "parking-management 주차장 지도 이미지 순서변경",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("주차장 지도 이미지 순서 변경 (ADMIN)")
                        .description("배열에 전달된 인덱스 순서대로 이미지 표시 순서를 재배치합니다.")
                        .requestSchema(Schema.schema("ParkingMapImageReqDto"))
                        .pathParameters(parameterWithName("id")
                                .description("주차 관리 ID")
                                .attributes(key("validationConstraints").value(List.of(ApiConstraint.JavaxMin2(1)))))
                        .requestHeaders(headerWithName("Authorization").description("Bearer {JWT}"))
                        .requestFields(
                                fieldWithPath("ids")
                                        .type(JsonFieldType.ARRAY)
                                        .description("이미지 id 리스트(배열 인덱스로 순서 변경함)")
                                        .attributes(key("validationConstraints")
                                                .value(List.of(ApiConstraint.JavaxMin2(1)))),
                                fieldWithPath("ids[].").ignored())
                        .build()));
    }

    public static RestDocumentationResultHandler deleteParkingMapImages() {
        return document(
                "parking-management 주차장 지도 이미지 삭제",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("주차장 지도 이미지 삭제 (ADMIN)")
                        .description("전달된 이미지 ID 목록을 삭제합니다. 전체삭제도 이 API 이용하기")
                        .requestSchema(Schema.schema("ParkingMapImageReqDto"))
                        .pathParameters(parameterWithName("id")
                                .description("주차 관리 ID")
                                .attributes(key("validationConstraints").value(List.of(ApiConstraint.JavaxMin2(1)))))
                        .requestHeaders(headerWithName("Authorization").description("Bearer {JWT}"))
                        .requestFields(fieldWithPath("ids")
                                .type(JsonFieldType.ARRAY)
                                .description("삭제할 이미지 ID 목록")
                                .attributes(key("validationConstraints").value(List.of(ApiConstraint.JavaxMin2(1)))))
                        .build()));
    }

    public static RestDocumentationResultHandler getOne() {
        return document(
                "parking-management ADMIN 단건조회",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("주차 관리 단건 조회 (ADMIN)")
                        .description("주차 관리 정보를 조회합니다.")
                        .responseSchema(Schema.schema("ParkingManagementResDto"))
                        .pathParameters(parameterWithName("id")
                                .description("주차 관리 ID")
                                .attributes(key("validationConstraints").value(List.of(ApiConstraint.JavaxMin2(1)))))
                        .responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("주차 관리 id"),
                                fieldWithPath("headerName")
                                        .type(JsonFieldType.STRING)
                                        .description("헤더명"),
                                fieldWithPath("parkingInfoType")
                                        .type(JsonFieldType.STRING)
                                        .description("표시 타입"),
                                fieldWithPath("title")
                                        .type(JsonFieldType.STRING)
                                        .description("타이틀")
                                        .optional(),
                                fieldWithPath("description")
                                        .type(JsonFieldType.STRING)
                                        .description("설명")
                                        .optional(),
                                fieldWithPath("buttonName")
                                        .type(JsonFieldType.STRING)
                                        .description("버튼명")
                                        .optional(),
                                fieldWithPath("buttonTargetUrl")
                                        .type(JsonFieldType.STRING)
                                        .description("버튼 링크 URL")
                                        .optional(),
                                fieldWithPath("backgroundImage")
                                        .type(JsonFieldType.STRING)
                                        .description("배경 이미지 URL")
                                        .optional(),
                                fieldWithPath("visible")
                                        .type(JsonFieldType.BOOLEAN)
                                        .description("노출 여부"))
                        .build()));
    }

    public static RestDocumentationResultHandler getOneForUser() {
        return document(
                "parking-management User 단건조회",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("주차 관리 단건 조회 (USER)")
                        .description("visible = true 상태인 주차 관리 정보를 조회합니다.")
                        .responseSchema(Schema.schema("ParkingManagementResDto"))
                        .pathParameters(parameterWithName("id")
                                .description("주차 관리 ID")
                                .attributes(key("validationConstraints").value(List.of(ApiConstraint.JavaxMin2(1)))))
                        .responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("주차 관리 id"),
                                fieldWithPath("headerName")
                                        .type(JsonFieldType.STRING)
                                        .description("헤더명"),
                                fieldWithPath("parkingInfoType")
                                        .type(JsonFieldType.STRING)
                                        .description("표시 타입"),
                                fieldWithPath("title")
                                        .type(JsonFieldType.STRING)
                                        .description("타이틀")
                                        .optional(),
                                fieldWithPath("description")
                                        .type(JsonFieldType.STRING)
                                        .description("설명")
                                        .optional(),
                                fieldWithPath("buttonName")
                                        .type(JsonFieldType.STRING)
                                        .description("버튼명")
                                        .optional(),
                                fieldWithPath("buttonTargetUrl")
                                        .type(JsonFieldType.STRING)
                                        .description("버튼 링크 URL")
                                        .optional(),
                                fieldWithPath("backgroundImage")
                                        .type(JsonFieldType.STRING)
                                        .description("배경 이미지 URL")
                                        .optional(),
                                fieldWithPath("visible")
                                        .type(JsonFieldType.BOOLEAN)
                                        .description("노출 여부"))
                        .build()));
    }

    public static RestDocumentationResultHandler getSubPageInfo() {
        return document(
                "parking-management 서브페이지 조회",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("서브페이지 정보 조회 (ADMIN)")
                        .description("서브페이지 헤더명과 주차장 지도 이미지 목록을 조회합니다. 주차 관리에서는 주차 지도 수정하는 페이지 정보 전달입니다.")
                        .responseSchema(Schema.schema("ParkingManagementSubPageResDto"))
                        .pathParameters(parameterWithName("id")
                                .description("주차 관리 ID")
                                .attributes(key("validationConstraints").value(List.of(ApiConstraint.JavaxMin2(1)))))
                        .requestHeaders(headerWithName("Authorization").description("Bearer {JWT}"))
                        .responseFields(
                                fieldWithPath("subPageHeaderName")
                                        .type(JsonFieldType.STRING)
                                        .description("서브페이지 헤더명"),
                                fieldWithPath("images")
                                        .type(JsonFieldType.ARRAY)
                                        .description("이미지 목록"),
                                fieldWithPath("images[].id")
                                        .type(JsonFieldType.NUMBER)
                                        .description("이미지 ID"),
                                fieldWithPath("images[].image")
                                        .type(JsonFieldType.STRING)
                                        .description("이미지 URL/식별자"))
                        .build()));
    }
}
