package com.halo.eventer.domain.inquiry.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.inquiry.Inquiry;
import com.halo.eventer.domain.inquiry.InquiryConstants;
import com.halo.eventer.domain.inquiry.api_docs.InquiryDoc;
import com.halo.eventer.domain.inquiry.dto.*;
import com.halo.eventer.domain.inquiry.service.InquiryService;
import com.halo.eventer.global.config.ControllerTestSecurityBeans;
import com.halo.eventer.global.config.security.SecurityConfig;
import com.halo.eventer.global.security.provider.JwtProvider;

import static com.halo.eventer.global.common.ApiErrorAssert.*;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.util.ReflectionTestUtils.setField;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InquiryController.class)
@AutoConfigureRestDocs
@SuppressWarnings("NonAsciiCharacters")
@ActiveProfiles("test")
@Import({ControllerTestSecurityBeans.class, SecurityConfig.class})
public class InquiryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private InquiryService inquiryService;

    @MockitoBean
    private JwtProvider jwtProvider;

    private final Long festivalId = 1L;
    private final Long inquiryId = 1L;

    List<Inquiry> inquiryList;
    Inquiry inquiry;
    Inquiry inquiry1;
    Inquiry inquiry2;
    Festival festival;
    InquiryCreateReqDto inquiryCreateReqDto;

    @BeforeEach
    void setUp() {
        inquiryCreateReqDto = new InquiryCreateReqDto("제목", true, "아이디", "1234", "내용");
        festival = new Festival();
        inquiry = new Inquiry(festival, inquiryCreateReqDto, "1234");
        setField(inquiry, "createdAt", LocalDateTime.now());
        inquiryList = makeInquiryList();
    }

    @Test
    void 문의사항_생성() throws Exception {
        // given
        doNothing().when(inquiryService).create(festivalId, inquiryCreateReqDto);

        // when & then
        mockMvc.perform(post("/inquiries")
                        .queryParam("festivalId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inquiryCreateReqDto)))
                .andExpect(status().isOk())
                .andDo(InquiryDoc.createInquiry());
    }

    @Test
    void 문의사항_생성시_비밀번호가_숫자_4자리가_아닐경우() throws Exception {
        // given
        InquiryCreateReqDto errorRequest = new InquiryCreateReqDto("제목", true, "아이디", "11111", "내용");

        // when & then
        ResultActions result = mockMvc.perform(post("/inquiries")
                        .param("festivalId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(errorRequest)))
                .andExpect(status().isBadRequest())
                .andDo(InquiryDoc.errorSnippet("문의사항_생성시_비밀번호가_4자리가_아닌경우"));
        assertError(result, "C013", "비밀번호는 숫자 4자리여야 합니다.", 400);
    }

    @Test
    void 문의사항_생성시_유저아이디_길이가_16을_넘을경우() throws Exception {
        // given
        String errorId = "abcdefghijklmnopqrstuv";
        InquiryCreateReqDto errorRequest = new InquiryCreateReqDto("제목", true, errorId, "1111", "내용");

        // when & then
        ResultActions result = mockMvc.perform(post("/inquiries")
                        .param("festivalId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(errorRequest)))
                .andExpect(status().isBadRequest())
                .andDo(InquiryDoc.errorSnippet("문의사항_생성시_유저_아이디_검증_오류"));
        assertError(result, "C013", "size must be between 1 and 16", 400);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 어드민용_문의사항_페이징_조회() throws Exception {
        // given
        InquiryNoOffsetPageDto inquiryNoOffsetPageDto = new InquiryNoOffsetPageDto(inquiryList, true);
        given(inquiryService.getAllInquiryForAdmin(festivalId, 0L)).willReturn(inquiryNoOffsetPageDto);
        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS");
        String expected1 = inquiry1.getCreatedAt().format(f);
        String expected2 = inquiry2.getCreatedAt().format(f);

        // then
        mockMvc.perform(get("/inquiries/forAdmin")
                        .param("festivalId", "1")
                        .param("lastId", "0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.inquiries", hasSize(2)))
                .andExpect(jsonPath("$.inquiries[0].id").value(inquiry1.getId()))
                .andExpect(jsonPath("$.inquiries[0].title").value(inquiry1.getTitle()))
                .andExpect(jsonPath("$.inquiries[0].isAnswered").value(inquiry1.isAnswered()))
                .andExpect(jsonPath("$.inquiries[0].isSecret").value(inquiry1.isSecret()))
                .andExpect(jsonPath("$.inquiries[0].userId").value(inquiry1.getUserId()))
                .andExpect(jsonPath("$.inquiries[0].createdAt").value(expected1))
                .andExpect(jsonPath("$.inquiries[1].id").value(inquiry2.getId()))
                .andExpect(jsonPath("$.inquiries[1].title").value(inquiry2.getTitle()))
                .andExpect(jsonPath("$.inquiries[1].isAnswered").value(inquiry2.isAnswered()))
                .andExpect(jsonPath("$.inquiries[1].isSecret").value(inquiry2.isSecret()))
                .andExpect(jsonPath("$.inquiries[1].userId").value(inquiry2.getUserId()))
                .andExpect(jsonPath("$.inquiries[1].createdAt").value(expected2))
                .andExpect(jsonPath("$.isLast").value(true))
                .andDo(InquiryDoc.getInquiriesForAdmin());
    }

    @Test
    void 일반사용자가_어드민용_문의사항_페이징_조회_인증거부() throws Exception {
        // when & then
        ResultActions result = mockMvc.perform(get("/inquiries/forAdmin")
                        .param("festivalId", "1")
                        .param("lastId", "0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andDo(InquiryDoc.errorSnippet("어드민용_문의사항_페이징조회_인증_거부"));
        assertError(result, "A002", "Unauthenticated", 401);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 어드민_문의사항_단일조회() throws Exception {
        // given
        given(inquiryService.findInquiryForAdmin(inquiryId)).willReturn(new InquiryResDto(inquiry));
        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS");
        String expectedDateTime = inquiry.getCreatedAt().format(f);

        // then
        mockMvc.perform(get("/inquiries/forAdmin/{inquiryId}", inquiryId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(inquiry.getTitle()))
                .andExpect(jsonPath("$.userId").value(inquiry.getUserId()))
                .andExpect(jsonPath("$.content").value(inquiry.getContent()))
                .andExpect(jsonPath("$.isAnswered").value(inquiry.isAnswered()))
                .andExpect(jsonPath("$.createdAt").value(expectedDateTime))
                .andExpect(jsonPath("$.answer").value(inquiry.getAnswer()))
                .andDo(InquiryDoc.getInquiryForAdmin());
    }

    @Test
    void 일반사용자_문의사항_단일조회_인증거부() throws Exception {
        // given
        given(inquiryService.findInquiryForAdmin(inquiryId)).willReturn(new InquiryResDto(inquiry));

        // then
        ResultActions result = mockMvc.perform(
                        get("/inquiries/forAdmin/{inquiryId}", inquiryId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andDo(InquiryDoc.errorSnippet("어드민용_문의사항_단일조회_인증_거부"));
        assertError(result, "A002", "Unauthenticated", 401);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 어드민_문의사항에_답변_달기() throws Exception {
        // given
        InquiryAnswerReqDto answerReqDto = new InquiryAnswerReqDto("답변");
        inquiry.registerAnswer("답변");
        given(inquiryService.updateInquiryAnswer(any(), any())).willReturn(new InquiryResDto(inquiry));
        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS");
        String expectedDateTime = inquiry.getCreatedAt().format(f);

        // then
        mockMvc.perform(patch("/inquiries/forAdmin")
                        .queryParam("inquiryId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(answerReqDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(inquiry.getTitle()))
                .andExpect(jsonPath("$.userId").value(inquiry.getUserId()))
                .andExpect(jsonPath("$.content").value(inquiry.getContent()))
                .andExpect(jsonPath("$.isAnswered").value(inquiry.isAnswered()))
                .andExpect(jsonPath("$.createdAt").value(expectedDateTime))
                .andExpect(jsonPath("$.answer").value(inquiry.getAnswer()))
                .andDo(InquiryDoc.answerInquiryForAdmin());
    }

    @Test
    void 권한없는_유저_문의사항에_답변_달기_인증거부() throws Exception {
        // given
        InquiryAnswerReqDto answerReqDto = new InquiryAnswerReqDto("답변");
        inquiry.registerAnswer("답변");
        given(inquiryService.updateInquiryAnswer(any(), any())).willReturn(new InquiryResDto(inquiry));

        // then
        ResultActions result = mockMvc.perform(patch("/inquiries/forAdmin")
                        .queryParam("inquiryId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(answerReqDto)))
                .andExpect(status().isUnauthorized())
                .andDo(InquiryDoc.errorSnippet("문의_답변_달기_인증_거부"));
        assertError(result, "A002", "Unauthenticated", 401);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 문의사항_삭제() throws Exception {
        // given
        doNothing().when(inquiryService).delete(inquiryId);

        // when & then
        mockMvc.perform(delete("/inquiries/forAdmin")
                        .queryParam("inquiryId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(InquiryDoc.deleteInquiryForAdmin());
        ;
    }

    @Test
    void 권한없는_유저_문의사항에_삭제_인증거부() throws Exception {
        // given
        doNothing().when(inquiryService).delete(inquiryId);

        // when & then
        mockMvc.perform(delete("/inquiries/forAdmin").param("inquiryId", "1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andDo(InquiryDoc.errorSnippet("문의사항_삭제_인증_거부"));
    }

    @Test
    void 일반_유저용_문의사항_리스트_조회() throws Exception {
        // given
        InquiryNoOffsetPageDto inquiryNoOffsetPageDto = new InquiryNoOffsetPageDto(inquiryList, true);
        given(inquiryService.getAllInquiryForUser(festivalId, 0L)).willReturn(inquiryNoOffsetPageDto);
        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS");
        String expected1 = inquiry1.getCreatedAt().format(f);
        String expected2 = inquiry2.getCreatedAt().format(f);
        inquiryNoOffsetPageDto.updateInquiryItemDtoWithTitleVisibility();

        // then
        mockMvc.perform(get("/inquiries/forUser")
                        .param("festivalId", "1")
                        .param("lastId", "0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.inquiries", hasSize(2)))
                .andExpect(jsonPath("$.inquiries[0].id").value(inquiry1.getId()))
                .andExpect(jsonPath("$.inquiries[0].title").value(InquiryConstants.PRIVATE_INQUIRY_TITLE))
                .andExpect(jsonPath("$.inquiries[0].isAnswered").value(inquiry1.isAnswered()))
                .andExpect(jsonPath("$.inquiries[0].isSecret").value(inquiry1.isSecret()))
                .andExpect(jsonPath("$.inquiries[0].userId").value(inquiry1.getUserId()))
                .andExpect(jsonPath("$.inquiries[0].createdAt").value(expected1))
                .andExpect(jsonPath("$.inquiries[1].id").value(inquiry2.getId()))
                .andExpect(jsonPath("$.inquiries[1].title").value(inquiry2.getTitle()))
                .andExpect(jsonPath("$.inquiries[1].isAnswered").value(inquiry2.isAnswered()))
                .andExpect(jsonPath("$.inquiries[1].isSecret").value(inquiry2.isSecret()))
                .andExpect(jsonPath("$.inquiries[1].userId").value(inquiry2.getUserId()))
                .andExpect(jsonPath("$.inquiries[1].createdAt").value(expected2))
                .andExpect(jsonPath("$.isLast").value(true))
                .andDo(InquiryDoc.getInquiriesForUser());
    }

    @Test
    void 일반유저_비밀글_접근() throws Exception {
        // given
        InquiryUserReqDto inquiryUserReqDto = new InquiryUserReqDto("아이디", "1323");
        given(inquiryService.getInquiryForUser(any(), any())).willReturn(new InquiryResDto(inquiry));
        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS");
        String expectedDateTime = inquiry.getCreatedAt().format(f);

        // then
        mockMvc.perform(post("/inquiries/forUser/{inquiryId}", inquiryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inquiryUserReqDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(inquiry.getTitle()))
                .andExpect(jsonPath("$.userId").value(inquiry.getUserId()))
                .andExpect(jsonPath("$.content").value(inquiry.getContent()))
                .andExpect(jsonPath("$.isAnswered").value(inquiry.isAnswered()))
                .andExpect(jsonPath("$.createdAt").value(expectedDateTime))
                .andExpect(jsonPath("$.answer").value(inquiry.getAnswer()))
                .andDo(InquiryDoc.getInquiryForUser());
    }

    private List<Inquiry> makeInquiryList() {
        inquiry1 = new Inquiry(festival, inquiryCreateReqDto, "1234");
        setField(inquiry1, "id", 1L);
        setField(inquiry1, "createdAt", LocalDateTime.now());
        inquiry2 = new Inquiry(festival, inquiryCreateReqDto, "1234");
        setField(inquiry2, "id", 2L);
        setField(inquiry2, "createdAt", LocalDateTime.now());
        setField(inquiry2, "isSecret", false);
        return new ArrayList<>(List.of(inquiry1, inquiry2));
    }
}
