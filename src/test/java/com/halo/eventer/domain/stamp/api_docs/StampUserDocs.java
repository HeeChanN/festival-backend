package com.halo.eventer.domain.stamp.api_docs;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

import com.epages.restdocs.apispec.ResourceSnippetParameters;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;

public class StampUserDocs {

    private static final String TAG = "스탬프투어 이용객";

    public static RestDocumentationResultHandler signup() {
        return document(
                "stampUser-회원가입 (커스텀 X)",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("스탬프 유저 회원가입 (커스텀 미포함)")
                        .description("stampId 로 스탬프에 참가자를 등록합니다.")
                        .queryParameters(parameterWithName("stampId")
                                .description("스탬프 ID")
                                .attributes(key("constraints").value("1 이상의 양의 정수")))
                        .requestFields(
                                fieldWithPath("name")
                                        .type(JsonFieldType.STRING)
                                        .description("참가자 이름")
                                        .attributes(key("constraints").value("NotEmpty")),
                                fieldWithPath("phone")
                                        .type(JsonFieldType.STRING)
                                        .description("참가자 전화번호")
                                        .attributes(key("constraints").value("NotEmpty, 전화번호 형식")),
                                fieldWithPath("participantCount")
                                        .type(JsonFieldType.NUMBER)
                                        .description("동반 인원 수")
                                        .attributes(key("constraints").value("1 이상")),
                                fieldWithPath("schoolNo")
                                        .type(JsonFieldType.STRING)
                                        .description("학교 식별 번호(커스텀 미포함 시 null)")
                                        .optional())
                        .responseFields(
                                fieldWithPath("uuid").type(JsonFieldType.STRING).description("유저 고유 식별자"),
                                fieldWithPath("finished")
                                        .type(JsonFieldType.BOOLEAN)
                                        .description("미션 완료 여부"),
                                fieldWithPath("participantCount")
                                        .type(JsonFieldType.NUMBER)
                                        .description("동반 인원 수"),
                                fieldWithPath("userMissionInfoGetDtos")
                                        .type(JsonFieldType.ARRAY)
                                        .description("유저의 미션 정보 리스트"),
                                fieldWithPath("userMissionInfoGetDtos[].userMissionId")
                                        .type(JsonFieldType.NUMBER)
                                        .description("사용자-미션 식별자"),
                                fieldWithPath("userMissionInfoGetDtos[].missionId")
                                        .type(JsonFieldType.NUMBER)
                                        .description("미션 ID"),
                                fieldWithPath("userMissionInfoGetDtos[].clear")
                                        .type(JsonFieldType.BOOLEAN)
                                        .description("해당 미션 클리어 여부"))
                        .build()));
    }

    public static RestDocumentationResultHandler signupCustom() {
        return document(
                "stampUser-회원가입 (커스텀 O)",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("스탬프 유저 회원가입(커스텀)")
                        .description("stampId 로 스탬프에 참가자를 등록합니다.")
                        .queryParameters(parameterWithName("stampId")
                                .description("스탬프 ID")
                                .attributes(key("constraints").value("1 이상의 양의 정수")))
                        .requestFields(
                                fieldWithPath("name")
                                        .type(JsonFieldType.STRING)
                                        .description("참가자 이름")
                                        .attributes(key("constraints").value("NotEmpty")),
                                fieldWithPath("phone")
                                        .type(JsonFieldType.STRING)
                                        .description("참가자 전화번호")
                                        .attributes(key("constraints").value("NotEmpty, 전화번호 형식")),
                                fieldWithPath("participantCount")
                                        .type(JsonFieldType.NUMBER)
                                        .description("동반 인원 수")
                                        .attributes(key("constraints").value("1 이상")),
                                fieldWithPath("schoolNo")
                                        .type(JsonFieldType.STRING)
                                        .description("학교 식별 번호 (커스텀 미포함 시 null)")
                                        .optional())
                        .responseFields(
                                fieldWithPath("uuid").type(JsonFieldType.STRING).description("유저 고유 식별자"),
                                fieldWithPath("finished")
                                        .type(JsonFieldType.BOOLEAN)
                                        .description("미션 완료 여부"),
                                fieldWithPath("participantCount")
                                        .type(JsonFieldType.NUMBER)
                                        .description("동반 인원 수"),
                                fieldWithPath("userMissionInfoGetDtos")
                                        .type(JsonFieldType.ARRAY)
                                        .description("유저의 미션 정보 리스트"),
                                fieldWithPath("userMissionInfoGetDtos[].userMissionId")
                                        .type(JsonFieldType.NUMBER)
                                        .description("사용자-미션 식별자"),
                                fieldWithPath("userMissionInfoGetDtos[].missionId")
                                        .type(JsonFieldType.NUMBER)
                                        .description("미션 ID"),
                                fieldWithPath("userMissionInfoGetDtos[].clear")
                                        .type(JsonFieldType.BOOLEAN)
                                        .description("해당 미션 클리어 여부"))
                        .build()));
    }

    public static RestDocumentationResultHandler signupV2() {
        return document(
                "stampUser-회원가입-v2",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("스탬프 유저 회원가입 v2")
                        .description("stampId 로 스탬프에 참가자를 등록합니다.")
                        .queryParameters(parameterWithName("stampId")
                                .description("스탬프 ID")
                                .attributes(key("constraints").value("1 이상의 양의 정수")))
                        .requestFields(
                                fieldWithPath("name")
                                        .type(JsonFieldType.STRING)
                                        .description("참가자 이름")
                                        .attributes(key("constraints").value("NotEmpty")),
                                fieldWithPath("phone")
                                        .type(JsonFieldType.STRING)
                                        .description("참가자 전화번호")
                                        .attributes(key("constraints").value("NotEmpty, 전화번호 형식")),
                                fieldWithPath("participantCount")
                                        .type(JsonFieldType.NUMBER)
                                        .description("동반 인원 수")
                                        .attributes(key("constraints").value("1 이상")),
                                fieldWithPath("schoolNo")
                                        .type(JsonFieldType.STRING)
                                        .description("학교 식별 번호(커스텀 미포함 시 null)")
                                        .optional())
                        .responseFields(
                                fieldWithPath("uuid").type(JsonFieldType.STRING).description("유저 고유 식별자"),
                                fieldWithPath("finished")
                                        .type(JsonFieldType.BOOLEAN)
                                        .description("미션 완료 여부"),
                                fieldWithPath("participantCount")
                                        .type(JsonFieldType.NUMBER)
                                        .description("동반 인원 수"),
                                fieldWithPath("userMissionInfoGetDtos")
                                        .type(JsonFieldType.ARRAY)
                                        .description("유저의 미션 정보 리스트"),
                                fieldWithPath("userMissionInfoGetDtos[].userMissionId")
                                        .type(JsonFieldType.NUMBER)
                                        .description("사용자-미션 식별자"),
                                fieldWithPath("userMissionInfoGetDtos[].missionId")
                                        .type(JsonFieldType.NUMBER)
                                        .description("미션 ID"),
                                fieldWithPath("userMissionInfoGetDtos[].clear")
                                        .type(JsonFieldType.BOOLEAN)
                                        .description("해당 미션 클리어 여부"))
                        .build()));
    }

    public static RestDocumentationResultHandler login() {
        return document(
                "stampUser-로그인",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("스탬프 유저 로그인")
                        .description("이름과 전화번호로 로그인합니다.")
                        .queryParameters(parameterWithName("stampId").description("스탬프 ID"))
                        .requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                                fieldWithPath("phone")
                                        .type(JsonFieldType.STRING)
                                        .description("전화번호"))
                        .responseFields(
                                fieldWithPath("uuid").type(JsonFieldType.STRING).description("UUID"),
                                fieldWithPath("participantCount")
                                        .type(JsonFieldType.NUMBER)
                                        .description("동반 인원 수"),
                                fieldWithPath("finished")
                                        .type(JsonFieldType.BOOLEAN)
                                        .description("미션 완료 여부"),
                                fieldWithPath("userMissionInfoGetDtos")
                                        .type(JsonFieldType.ARRAY)
                                        .description("미션 리스트"),
                                fieldWithPath("userMissionInfoGetDtos[].userMissionId")
                                        .type(JsonFieldType.NUMBER)
                                        .description("유저-미션 ID"),
                                fieldWithPath("userMissionInfoGetDtos[].missionId")
                                        .type(JsonFieldType.NUMBER)
                                        .description("미션 ID"),
                                fieldWithPath("userMissionInfoGetDtos[].clear")
                                        .type(JsonFieldType.BOOLEAN)
                                        .description("클리어 여부"))
                        .build()));
    }

    public static RestDocumentationResultHandler getMissionInfo() {
        return document(
                "stampUser-미션 현황 조회",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("스탬프 유저 미션 현황 조회")
                        .pathParameters(parameterWithName("uuid").description("유저 UUID"))
                        .requestHeaders(headerWithName("Authorization").description("JWT Access 토큰"))
                        .responseFields(
                                fieldWithPath("finished")
                                        .type(JsonFieldType.BOOLEAN)
                                        .description("전체 미션 완료 여부"),
                                fieldWithPath("userMissionInfoGetDtos")
                                        .type(JsonFieldType.ARRAY)
                                        .description("미션 목록"),
                                fieldWithPath("userMissionInfoGetDtos[].userMissionId")
                                        .type(JsonFieldType.NUMBER)
                                        .description("유저 미션 ID"),
                                fieldWithPath("userMissionInfoGetDtos[].missionId")
                                        .type(JsonFieldType.NUMBER)
                                        .description("미션 ID"),
                                fieldWithPath("userMissionInfoGetDtos[].clear")
                                        .type(JsonFieldType.BOOLEAN)
                                        .description("미션 완료 여부"))
                        .build()));
    }

    public static RestDocumentationResultHandler updateUserMission() {
        return document(
                "stamp-user-update-mission",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("스탬프 유저 미션 완료 처리")
                        .pathParameters(
                                parameterWithName("uuid").description("유저 UUID"),
                                parameterWithName("userMissionId").description("유저 미션 ID"))
                        .build()));
    }

    public static RestDocumentationResultHandler checkFinish() {
        return document(
                "stamp-user-check-finish",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("스탬프 유저 전체미션 완료여부 갱신")
                        .description(
                                """
                                UUID에 해당하는 사용자의 전체 미션 완료 여부를 갱신합니다.\s
                                응답 : 미완료 or 스탬프 투어 완료
                                """)
                        .pathParameters(parameterWithName("uuid").description("유저 UUID"))
                        .build()));
    }

    public static RestDocumentationResultHandler checkFinishV2() {
        return document(
                "stamp-user-check-finish-v2",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("스탬프 유저 전체미션 완료여부 갱신 v2")
                        .description(
                                """
                                UUID에 해당하는 사용자의 전체 미션 완료 여부를 갱신합니다.\s
                                응답 : 미완료 or 스탬프 투어 완료
                                """)
                        .pathParameters(parameterWithName("uuid").description("유저 UUID"))
                        .build()));
    }

    public static RestDocumentationResultHandler getStampUsers() {
        return document(
                "stamp-user-list",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("스탬프 유저 전체 조회")
                        .queryParameters(parameterWithName("stampId").description("스탬프 ID"))
                        .requestHeaders(headerWithName("Authorization").description("JWT Access 토큰"))
                        .responseFields(
                                fieldWithPath("[].uuid")
                                        .type(JsonFieldType.STRING)
                                        .description("UUID"),
                                fieldWithPath("[].name")
                                        .type(JsonFieldType.STRING)
                                        .description("이름"),
                                fieldWithPath("[].phone")
                                        .type(JsonFieldType.STRING)
                                        .description("전화번호"),
                                fieldWithPath("[].finished")
                                        .type(JsonFieldType.BOOLEAN)
                                        .description("미션 완료 여부"),
                                fieldWithPath("[].participantCount")
                                        .type(JsonFieldType.NUMBER)
                                        .description("동반 인원 수"))
                        .build()));
    }

    public static RestDocumentationResultHandler deleteStampUser() {
        return document(
                "stamp-user-delete",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("스탬프 유저 삭제")
                        .pathParameters(parameterWithName("uuid").description("유저 UUID"))
                        .requestHeaders(headerWithName("Authorization").description("JWT Access 토큰"))
                        .build()));
    }

    public static RestDocumentationResultHandler errorSnippet(String identifier) {
        return document(
                identifier,
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .responseFields(
                                fieldWithPath("code").description("에러 코드"),
                                fieldWithPath("message").description("에러 상세"),
                                fieldWithPath("status").description("HTTP 상태"))
                        .build()));
    }
}
