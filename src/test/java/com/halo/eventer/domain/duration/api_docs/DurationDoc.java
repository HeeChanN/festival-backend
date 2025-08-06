package com.halo.eventer.domain.duration.api_docs;

import java.util.List;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.halo.eventer.global.constants.ApiConstraint;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.*;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.snippet.Attributes.key;

public class DurationDoc {

    private static final String TAG = "축제 기간";

    public static RestDocumentationResultHandler createDuration() {
        return document(
                "duration 생성",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("기간 생성")
                        .description("축제 진행 날짜와 일차로 기간 생성")
                        .requestHeaders(headerWithName("Authorization").description("JWT Access 토큰"))
                        .queryParameters(parameterWithName("festivalId")
                                .description("축제 id")
                                .attributes(key("validationConstraints").value(List.of(ApiConstraint.JavaxNotNull()))))
                        .requestFields(
                                fieldWithPath("[].date")
                                        .type(JsonFieldType.STRING)
                                        .description("축제 날짜 (yyyy-MM-dd)")
                                        .attributes(key("validationConstraints")
                                                .value(List.of(ApiConstraint.JavaxNotNull()))),
                                fieldWithPath("[].dayNumber")
                                        .type(JsonFieldType.NUMBER)
                                        .description("축제 일차 (n일차)")
                                        .attributes(
                                                key("validationConstraints").value(List.of(ApiConstraint.JavaxMin()))))
                        .build()));
    }

    public static RestDocumentationResultHandler getDurations() {
        return document(
                "duration 리스트 조회",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("기간 리스트 조회")
                        .description("축제 id로 등록된 기간 리스트 조회")
                        .queryParameters(parameterWithName("festivalId")
                                .description("축제 id")
                                .attributes(key("constraints").value("필수, 1 이상의 양의 정수")))
                        .responseFields(
                                fieldWithPath("[]").description("기간(Duration) 목록"),
                                fieldWithPath("[].durationId")
                                        .type(JsonFieldType.NUMBER)
                                        .description("기간 식별자(ID)"),
                                fieldWithPath("[].date")
                                        .type(JsonFieldType.STRING)
                                        .description("해당 기간 날짜 (yyyy-MM-dd)"),
                                fieldWithPath("[].dayNumber")
                                        .type(JsonFieldType.NUMBER)
                                        .description("축제 시작일 기준 N번째 날 (1부터 시작)"))
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
