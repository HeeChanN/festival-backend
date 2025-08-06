package com.halo.eventer.domain.notice.controller;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.halo.eventer.domain.notice.ArticleType;
import com.halo.eventer.domain.notice.NoticeFixture;
import com.halo.eventer.domain.notice.controller.admin.AdminNoticeController;
import com.halo.eventer.domain.notice.dto.*;
import com.halo.eventer.domain.notice.service.NoticeService;
import com.halo.eventer.global.common.page.PagedResponse;
import com.halo.eventer.global.config.ControllerTestSecurityBeans;
import com.halo.eventer.global.config.security.SecurityConfig;
import com.halo.eventer.global.security.provider.JwtProvider;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminNoticeController.class)
@AutoConfigureMockMvc
@SuppressWarnings("NonAsciiCharacters")
@ActiveProfiles("test")
@Import({ControllerTestSecurityBeans.class, SecurityConfig.class})
public class AdminNoticeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private NoticeService noticeService;

    @MockitoBean
    private JwtProvider jwtProvider;

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void notice_생성_api_성공_테스트() throws Exception {
        // given
        NoticeCreateReqDto noticeCreateReqDto = NoticeFixture.공지사항_생성_DTO();
        NoticeResDto responseDto = NoticeResDto.builder()
                .title(noticeCreateReqDto.getTitle())
                .content(noticeCreateReqDto.getContent())
                .type(noticeCreateReqDto.getType())
                .build();
        given(noticeService.create(eq(1L), any(NoticeCreateReqDto.class))).willReturn(responseDto);

        // when & then
        mockMvc.perform(post("/admin/notices")
                        .param("festivalId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(noticeCreateReqDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(noticeCreateReqDto.getTitle()))
                .andExpect(jsonPath("$.type").value(noticeCreateReqDto.getType().toString()));
    }

    @Test
    void notice_생성_api_권한_없음() throws Exception {
        NoticeCreateReqDto noticeCreateReqDto = NoticeFixture.공지사항_생성_DTO();
        NoticeResDto responseDto = NoticeResDto.builder()
                .title(noticeCreateReqDto.getTitle())
                .content(noticeCreateReqDto.getContent())
                .type(noticeCreateReqDto.getType())
                .build();
        given(noticeService.create(eq(1L), any(NoticeCreateReqDto.class))).willReturn(responseDto);

        mockMvc.perform(post("/admin/notices")
                        .param("festivalId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(noticeCreateReqDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void notice_단일_조회_성공() throws Exception {
        NoticeResDto responseDto = NoticeResDto.builder()
                .title("title")
                .content("content")
                .type(ArticleType.NOTICE)
                .build();
        given(noticeService.getNoticeById(eq(1L))).willReturn(responseDto);

        mockMvc.perform(get("/admin/notices/{noticeId}", 1L).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.type").value(ArticleType.NOTICE.toString()));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void notice_페이징_조회_성공() throws Exception {
        // given
        PagedResponse<NoticeSummaryDto> response = new PagedResponse<>();
        given(noticeService.getNoticesByType(eq(1L), any(ArticleType.class), eq(0), eq(10)))
                .willReturn(response);

        // when & then
        mockMvc.perform(get("/admin/notices")
                        .param("festivalId", "1")
                        .param("type", "NOTICE")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void notice_페이징_조회_ArticleType_예외() throws Exception {
        // given
        PagedResponse<NoticeSummaryDto> response = new PagedResponse<>();
        given(noticeService.getNoticesByType(eq(1L), any(ArticleType.class), eq(0), eq(10)))
                .willReturn(response);

        // when & then
        mockMvc.perform(get("/admin/notices")
                        .param("festivalId", "1")
                        .param("type", "NOTICEE")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void banner_선택_api_성공() throws Exception {
        NoticeResDto response = NoticeResDto.builder()
                .title("title")
                .content("content")
                .type(ArticleType.NOTICE)
                .build();
        given(noticeService.updatePicked(eq(1L), eq(true))).willReturn(response);

        // when & then
        mockMvc.perform(post("/admin/notices/banner")
                        .param("noticeId", "1")
                        .param("picked", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(response.getTitle()))
                .andExpect(jsonPath("$.content").value(response.getContent()))
                .andExpect(jsonPath("$.type").value(response.getType().toString()));
    }

    @Test
    void banner_선택_api_권한_예외() throws Exception {
        NoticeResDto response = NoticeResDto.builder()
                .title("title")
                .content("content")
                .type(ArticleType.NOTICE)
                .build();
        given(noticeService.updatePicked(eq(1L), eq(true))).willReturn(response);

        // when & then
        mockMvc.perform(post("/admin/notices/banner")
                        .param("noticeId", "1")
                        .param("picked", "true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 선택된_notice_리스트_조회_성공() throws Exception {
        PickedNoticeResDto n1 = new PickedNoticeResDto();
        PickedNoticeResDto n2 = new PickedNoticeResDto();
        List<PickedNoticeResDto> response = List.of(n1, n2);
        given(noticeService.getPickedNotice(eq(1L))).willReturn(response);

        // when & then
        mockMvc.perform(get("/admin/notices/banner").param("festivalId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void picked_notice_순서_변경_api_성공() throws Exception {
        PickedNoticeResDto n1 = new PickedNoticeResDto();
        PickedNoticeResDto n2 = new PickedNoticeResDto();
        PickedNoticeUpdateDto n3 = new PickedNoticeUpdateDto();
        PickedNoticeUpdateDto n4 = new PickedNoticeUpdateDto();
        List<PickedNoticeResDto> response = List.of(n1, n2);
        List<PickedNoticeUpdateDto> request = List.of(n3, n4);
        given(noticeService.updateDisplayOrder(anyList())).willReturn(response);

        mockMvc.perform(patch("/admin/notices/banner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void picked_notice_순서_변경_api_권한_예외() throws Exception {
        PickedNoticeResDto n1 = new PickedNoticeResDto();
        PickedNoticeResDto n2 = new PickedNoticeResDto();
        PickedNoticeUpdateDto n3 = new PickedNoticeUpdateDto();
        PickedNoticeUpdateDto n4 = new PickedNoticeUpdateDto();
        List<PickedNoticeResDto> response = List.of(n1, n2);
        List<PickedNoticeUpdateDto> request = List.of(n3, n4);
        given(noticeService.updateDisplayOrder(anyList())).willReturn(response);

        mockMvc.perform(patch("/admin/notices/banner")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void notice_수정_API_성공() throws Exception {
        NoticeUpdateReqDto noticeUpdateReqDto = NoticeFixture.공지사항_수정_DTO();
        NoticeResDto responseDto = NoticeResDto.builder()
                .title(noticeUpdateReqDto.getTitle())
                .content(noticeUpdateReqDto.getContent())
                .type(noticeUpdateReqDto.getType())
                .build();
        given(noticeService.updateNotice(eq(1L), any(NoticeUpdateReqDto.class))).willReturn(responseDto);

        mockMvc.perform(patch("/admin/notices/{noticeId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(noticeUpdateReqDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(noticeUpdateReqDto.getTitle()))
                .andExpect(jsonPath("$.content").value(noticeUpdateReqDto.getContent()))
                .andExpect(jsonPath("$.type").value(noticeUpdateReqDto.getType().toString()));
    }

    @Test
    void notice_수정_API_권한_예외() throws Exception {
        NoticeUpdateReqDto noticeUpdateReqDto = NoticeFixture.공지사항_수정_DTO();
        NoticeResDto responseDto = NoticeResDto.builder()
                .title(noticeUpdateReqDto.getTitle())
                .content(noticeUpdateReqDto.getContent())
                .type(noticeUpdateReqDto.getType())
                .build();
        given(noticeService.updateNotice(eq(1L), any(NoticeUpdateReqDto.class))).willReturn(responseDto);

        mockMvc.perform(patch("/admin/notices/{noticeId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(noticeUpdateReqDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void notice_삭제_api_성공() throws Exception {
        // given
        doNothing().when(noticeService).delete(eq(1L));

        // then
        mockMvc.perform(delete("/admin/notices/{noticeId}", 1L).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(noticeService, times(1)).delete(eq(1L));
    }

    @Test
    void notice_삭제_api_권한예외() throws Exception {
        // given
        doNothing().when(noticeService).delete(eq(1L));

        // then
        mockMvc.perform(delete("/admin/notices/{noticeId}", 1L).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
