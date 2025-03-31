package com.halo.eventer.domain.inquiry.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.inquiry.Inquiry;
import com.halo.eventer.domain.inquiry.dto.*;
import com.halo.eventer.domain.inquiry.service.InquiryService;
import com.halo.eventer.global.config.ControllerTestSecurityBeans;
import com.halo.eventer.global.config.security.SecurityConfig;
import com.halo.eventer.global.security.provider.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InquiryController.class)
@AutoConfigureMockMvc
@SuppressWarnings("NonAsciiCharacters")
@ActiveProfiles("test")
@Import({ControllerTestSecurityBeans.class, SecurityConfig.class})
public class InquiryControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private InquiryService inquiryService;

  @MockBean private JwtProvider jwtProvider;

  private final Long festivalId = 1L;
  private final Long inquiryId = 1L;

  List<Inquiry> inquiryList;
  Inquiry inquiry;
  Festival festival;
  InquiryCreateReqDto inquiryCreateReqDto;

  @BeforeEach
  void setUp() {
    inquiryCreateReqDto = new InquiryCreateReqDto("제목", true, "아이디", "1234", "내용");
    festival = new Festival();
    inquiry = new Inquiry(festival, inquiryCreateReqDto, "1234");
    inquiryList = makeInquiryList();
  }

  @Test
  void 문의사항_생성() throws Exception {
    // given
    doNothing().when(inquiryService).create(festivalId, inquiryCreateReqDto);

    // when & then
    mockMvc.perform(post("/inquiry")
                .param("festivalId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inquiryCreateReqDto)))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "admin", roles = "ADMIN")
  void 어드민용_문의사항_페이징_조회() throws Exception {
    // given
    InquiryNoOffsetPageDto inquiryNoOffsetPageDto = new InquiryNoOffsetPageDto(inquiryList, false);
    given(inquiryService.getAllInquiryForAdmin(festivalId, 0L)).willReturn(inquiryNoOffsetPageDto);

    // then
    mockMvc.perform(get("/inquiry/forAdmin")
                .param("festivalId", "1")
                .param("lastId", "0")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.inquiries", hasSize(10)))
        .andExpect(jsonPath("$.isLast").value(false));
  }

  @Test
  @WithMockUser(username = "user", roles = "USER")
  void 권한이없는_계정으로_문의사항_페이징_조회_인가거부() throws Exception {
    // given
    InquiryNoOffsetPageDto inquiryNoOffsetPageDto = new InquiryNoOffsetPageDto(inquiryList, false);
    given(inquiryService.getAllInquiryForAdmin(festivalId, 0L)).willReturn(inquiryNoOffsetPageDto);

    // then
    mockMvc.perform(get("/inquiry/forAdmin")
                .param("festivalId", "1")
                .param("lastId", "0")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }

  @Test
  void 일반사용자가_어드민용_문의사항_페이징_조회_인증거부() throws Exception {
    // given
    InquiryNoOffsetPageDto inquiryNoOffsetPageDto = new InquiryNoOffsetPageDto(inquiryList, false);
    given(inquiryService.getAllInquiryForAdmin(festivalId, 0L)).willReturn(inquiryNoOffsetPageDto);

    // then
    mockMvc.perform(get("/inquiry/forAdmin")
                .param("festivalId", "1")
                .param("lastId", "0")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "admin", roles = "ADMIN")
  void 어드민_문의사항_단일조회() throws Exception {
    // given
    given(inquiryService.findInquiryForAdmin(inquiryId)).willReturn(new InquiryResDto(inquiry));

    // then
    mockMvc.perform(get("/inquiry/forAdmin/{inquiryId}", inquiryId).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("제목"))
        .andExpect(jsonPath("$.userId").value("아이디"))
        .andExpect(jsonPath("$.content").value("내용"));
  }

  @Test
  @WithMockUser(username = "user", roles = "USER")
  void 권한없는_사용자_문의사항_단일조회_인가거부() throws Exception {
    // given
    given(inquiryService.findInquiryForAdmin(inquiryId)).willReturn(new InquiryResDto(inquiry));

    // then
    mockMvc.perform(get("/inquiry/forAdmin/{inquiryId}", inquiryId).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }

  @Test
  void 일반사용자_문의사항_단일조회_인증거부() throws Exception {
    // given
    given(inquiryService.findInquiryForAdmin(inquiryId)).willReturn(new InquiryResDto(inquiry));

    // then
    mockMvc.perform(get("/inquiry/forAdmin/{inquiryId}", inquiryId).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "admin", roles = "ADMIN")
  void 어드민_문의사항에_답변_달기() throws Exception {
    // given
    InquiryAnswerReqDto answerReqDto = new InquiryAnswerReqDto("답변");
    inquiry.registerAnswer("답변");
    given(inquiryService.updateInquiryAnswer(any(), any())).willReturn(new InquiryResDto(inquiry));

    // then
    mockMvc.perform(patch("/inquiry/forAdmin")
                .param("inquiryId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(answerReqDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.answer").value("답변"));
  }

  @Test
  @WithMockUser(username = "user", roles = "USER")
  void 권한없는_유저_문의사항에_답변_달기_인가거부() throws Exception {
    // given
    InquiryAnswerReqDto answerReqDto = new InquiryAnswerReqDto("답변");
    inquiry.registerAnswer("답변");
    given(inquiryService.updateInquiryAnswer(any(), any())).willReturn(new InquiryResDto(inquiry));

    // then
    mockMvc.perform(patch("/inquiry/forAdmin")
                .param("inquiryId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(answerReqDto)))
        .andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(username = "admin", roles = "ADMIN")
  void 문의사항_삭제() throws Exception {
    // given
    doNothing().when(inquiryService).delete(inquiryId);

    // when & then
    mockMvc.perform(delete("/inquiry/forAdmin")
                .param("inquiryId", "1")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "user", roles = "USER")
  void 권한없는_유저_문의사항에_삭제_인가거부() throws Exception {
    // given
    doNothing().when(inquiryService).delete(inquiryId);

    // when & then
    mockMvc.perform(delete("/inquiry/forAdmin")
                .param("inquiryId", "1")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }

  @Test
  void 일반_유저용_문의사항_리스트_조회() throws Exception {
    // given
    InquiryNoOffsetPageDto inquiryNoOffsetPageDto = new InquiryNoOffsetPageDto(inquiryList, false);
    given(inquiryService.getAllInquiryForUser(festivalId, 0L)).willReturn(inquiryNoOffsetPageDto);

    // then
    mockMvc.perform(get("/inquiry/forUser")
                .param("festivalId", "1")
                .param("lastId", "0")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.inquiries", hasSize(10)))
        .andExpect(jsonPath("$.isLast").value(false));
  }

  @Test
  void 일반유저_비밀글_접근() throws Exception {
    // given
    InquiryUserReqDto inquiryUserReqDto = new InquiryUserReqDto("아이디", "비밀번호");
    given(inquiryService.getInquiryForUser(any(), any())).willReturn(new InquiryResDto(inquiry));

    // then
    mockMvc.perform(post("/inquiry/forUser/{inquiryId}", inquiryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inquiryUserReqDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.userId").value("아이디"));
  }

  private List<Inquiry> makeInquiryList() {
    List<Inquiry> inquiryItemDtoList = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      inquiryItemDtoList.add(inquiry);
    }
    return inquiryItemDtoList;
  }
}
