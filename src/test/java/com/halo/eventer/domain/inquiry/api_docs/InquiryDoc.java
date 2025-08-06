package com.halo.eventer.domain.inquiry.api_docs;

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

public class InquiryDoc {

    private static final String TAG = "문의사항";

    public static RestDocumentationResultHandler createInquiry() {
        return document(
                "inquiry 생성",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("문의사항 생성")
                        .description("축제 ID와 함께 문의사항을 생성합니다.")
                        .queryParameters(parameterWithName("festivalId")
                                .description("축제 id")
                                .attributes(key("validationConstraints").value(List.of(ApiConstraint.JavaxMin()))))
                        .requestFields(
                                fieldWithPath("title")
                                        .type(JsonFieldType.STRING)
                                        .description("제목")
                                        .attributes(key("validationConstraints")
                                                .value(List.of(ApiConstraint.JavaxNotNull()))),
                                fieldWithPath("isSecret")
                                        .type(JsonFieldType.BOOLEAN)
                                        .description("비밀글 여부"),
                                fieldWithPath("userId")
                                        .type(JsonFieldType.STRING)
                                        .description("작성자 식별자(최대 16자)")
                                        .attributes(key("validationConstraints")
                                                .value(List.of(ApiConstraint.JavaxSize(1, 16)))),
                                fieldWithPath("password")
                                        .type(JsonFieldType.STRING)
                                        .description("비밀번호 (숫자 4자리)")
                                        .attributes(key("validationConstraints")
                                                .value(List.of(ApiConstraint.JavaxPattern("\\d{4}")))),
                                fieldWithPath("content")
                                        .type(JsonFieldType.STRING)
                                        .description("내용(최대 500자)")
                                        .attributes(key("validationConstraints")
                                                .value(List.of(
                                                        ApiConstraint.JavaxNotNull(), ApiConstraint.JavaxMax(500)))))
                        .build()));
    }

    public static RestDocumentationResultHandler getInquiriesForAdmin() {
        return document(
                "inquiry 어드민 리스트 조회",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("어드민용 문의사항 페이징 조회")
                        .description("축제 ID와 마지막 조회 ID 기준으로 문의사항을 페이징 조회합니다. size 크기는 20으로 고정되어 있습니다.")
                        .queryParameters(
                                parameterWithName("festivalId")
                                        .description("축제 id")
                                        .attributes(key("validationConstraints")
                                                .value(List.of(ApiConstraint.JavaxMin2(1)))),
                                parameterWithName("lastId")
                                        .description("마지막 항목 ID(초기 0)")
                                        .attributes(key("validationConstraints")
                                                .value(List.of(ApiConstraint.JavaxMin2(0)))))
                        .responseFields(
                                fieldWithPath("inquiries[]")
                                        .type(JsonFieldType.ARRAY)
                                        .description("문의사항 목록"),
                                fieldWithPath("inquiries[].id")
                                        .type(JsonFieldType.NUMBER)
                                        .description("문의사항 id"),
                                fieldWithPath("inquiries[].title")
                                        .type(JsonFieldType.STRING)
                                        .description("제목"),
                                fieldWithPath("inquiries[].isAnswered")
                                        .type(JsonFieldType.BOOLEAN)
                                        .description("답변 여부"),
                                fieldWithPath("inquiries[].isSecret")
                                        .type(JsonFieldType.BOOLEAN)
                                        .description("비밀글 여부"),
                                fieldWithPath("inquiries[].userId")
                                        .type(JsonFieldType.STRING)
                                        .description("작성자 식별자"),
                                fieldWithPath("inquiries[].createdAt")
                                        .type(JsonFieldType.STRING)
                                        .description("생성일시 (ISO-8601, 나노초 포함 가능)"),
                                fieldWithPath("isLast")
                                        .type(JsonFieldType.BOOLEAN)
                                        .description("마지막 페이지 여부"))
                        .build()));
    }

    public static RestDocumentationResultHandler getInquiryForAdmin() {
        return document(
                "inquiry 어드민 단일 조회",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("어드민용 문의사항 단일 조회")
                        .description("문의사항 ID로 상세 정보를 조회합니다.")
                        .pathParameters(parameterWithName("inquiryId")
                                .description("문의사항 id")
                                .attributes(key("constraints").value("필수, 1 이상의 양의 정수")))
                        .responseFields(
                                fieldWithPath("title")
                                        .type(JsonFieldType.STRING)
                                        .description("제목"),
                                fieldWithPath("userId")
                                        .type(JsonFieldType.STRING)
                                        .description("작성자 식별자"),
                                fieldWithPath("content")
                                        .type(JsonFieldType.STRING)
                                        .description("내용"),
                                fieldWithPath("isAnswered")
                                        .type(JsonFieldType.BOOLEAN)
                                        .description("답변 여부"),
                                fieldWithPath("createdAt")
                                        .type(JsonFieldType.STRING)
                                        .description("생성일시 (ISO-8601)"),
                                fieldWithPath("answer")
                                        .type(JsonFieldType.STRING)
                                        .description("관리자 답변")
                                        .optional())
                        .build()));
    }

    public static RestDocumentationResultHandler answerInquiryForAdmin() {
        return document(
                "inquiry 어드민 답변 등록",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("어드민 답변 등록/수정")
                        .description("문의사항에 대한 관리자의 답변을 등록/수정합니다.")
                        // 테스트는 .param → requestParameters 사용
                        .queryParameters(parameterWithName("inquiryId")
                                .description("문의사항 id")
                                .attributes(key("validationConstraints")
                                        .value(List.of(ApiConstraint.JavaxNotNull(), ApiConstraint.JavaxMin()))))
                        .requestFields(fieldWithPath("answer")
                                .type(JsonFieldType.STRING)
                                .description("답변 내용")
                                .attributes(key("validationConstraints")
                                        .value(List.of(ApiConstraint.JavaxNotNull(), ApiConstraint.JavaxSize(1, 500)))))
                        .responseFields(
                                fieldWithPath("title")
                                        .type(JsonFieldType.STRING)
                                        .description("제목"),
                                fieldWithPath("userId")
                                        .type(JsonFieldType.STRING)
                                        .description("작성자 식별자"),
                                fieldWithPath("content")
                                        .type(JsonFieldType.STRING)
                                        .description("내용"),
                                fieldWithPath("isAnswered")
                                        .type(JsonFieldType.BOOLEAN)
                                        .description("답변 여부"),
                                fieldWithPath("createdAt")
                                        .type(JsonFieldType.STRING)
                                        .description("생성일시 (ISO-8601)"),
                                fieldWithPath("answer")
                                        .type(JsonFieldType.STRING)
                                        .description("관리자 답변"))
                        .build()));
    }

    public static RestDocumentationResultHandler deleteInquiryForAdmin() {
        return document(
                "inquiry 어드민 삭제",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("어드민용 문의사항 삭제")
                        .description("문의사항을 삭제합니다.")
                        .queryParameters(parameterWithName("inquiryId")
                                .description("문의사항 id")
                                .attributes(key("validationConstraints").value(List.of(ApiConstraint.JavaxMin()))))
                        .build()));
    }

    public static RestDocumentationResultHandler getInquiriesForUser() {
        return document(
                "inquiry 사용자 리스트 조회",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("일반 사용자용 문의사항 페이징 조회")
                        .description("축제 ID와 마지막 조회 ID 기준으로 문의사항을 페이징 조회합니다. 비밀글은 제목이 마스킹(HIDDEN)될 수 있습니다.")
                        .queryParameters(
                                parameterWithName("festivalId")
                                        .description("축제 id")
                                        .attributes(
                                                key("validationConstraints").value(List.of(ApiConstraint.JavaxMin()))),
                                parameterWithName("lastId")
                                        .description("마지막 항목 ID(초기 0)")
                                        .attributes(key("validationConstraints")
                                                .value(List.of(ApiConstraint.JavaxMin2(0)))))
                        .responseFields(
                                fieldWithPath("inquiries[]")
                                        .type(JsonFieldType.ARRAY)
                                        .description("문의사항 목록"),
                                fieldWithPath("inquiries[].id")
                                        .type(JsonFieldType.NUMBER)
                                        .description("문의사항 id"),
                                fieldWithPath("inquiries[].title")
                                        .type(JsonFieldType.STRING)
                                        .description("제목(비밀글은 마스킹됨)"),
                                fieldWithPath("inquiries[].isAnswered")
                                        .type(JsonFieldType.BOOLEAN)
                                        .description("답변 여부"),
                                fieldWithPath("inquiries[].isSecret")
                                        .type(JsonFieldType.BOOLEAN)
                                        .description("비밀글 여부"),
                                fieldWithPath("inquiries[].userId")
                                        .type(JsonFieldType.STRING)
                                        .description("작성자 식별자"),
                                fieldWithPath("inquiries[].createdAt")
                                        .type(JsonFieldType.STRING)
                                        .description("생성일시 (ISO-8601)"),
                                fieldWithPath("isLast")
                                        .type(JsonFieldType.BOOLEAN)
                                        .description("마지막 페이지 여부"))
                        .build()));
    }

    public static RestDocumentationResultHandler getInquiryForUser() {
        return document(
                "inquiry 사용자 단일 조회(비밀글 접근)",
                resource(ResourceSnippetParameters.builder()
                        .tag(TAG)
                        .summary("일반 사용자 비밀글 접근")
                        .description("작성자 ID와 비밀번호로 비밀글을 조회합니다.")
                        .pathParameters(parameterWithName("inquiryId")
                                .description("문의사항 id")
                                .attributes(key("constraints").value("필수, 1 이상의 양의 정수")))
                        .requestFields(
                                fieldWithPath("userId")
                                        .type(JsonFieldType.STRING)
                                        .description("작성자 식별자")
                                        .attributes(key("validationConstraints")
                                                .value(List.of(ApiConstraint.JavaxSize(1, 16)))),
                                fieldWithPath("password")
                                        .type(JsonFieldType.STRING)
                                        .description("열람 비밀번호 (숫자 4자리)")
                                        .attributes(key("validationConstraints")
                                                .value(List.of(ApiConstraint.JavaxPattern("\\d{4}")))))
                        .responseFields(
                                fieldWithPath("title")
                                        .type(JsonFieldType.STRING)
                                        .description("제목"),
                                fieldWithPath("userId")
                                        .type(JsonFieldType.STRING)
                                        .description("작성자 식별자"),
                                fieldWithPath("content")
                                        .type(JsonFieldType.STRING)
                                        .description("내용"),
                                fieldWithPath("isAnswered")
                                        .type(JsonFieldType.BOOLEAN)
                                        .description("답변 여부"),
                                fieldWithPath("createdAt")
                                        .type(JsonFieldType.STRING)
                                        .description("생성일시 (ISO-8601)"),
                                fieldWithPath("answer")
                                        .type(JsonFieldType.STRING)
                                        .description("관리자 답변")
                                        .optional())
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
