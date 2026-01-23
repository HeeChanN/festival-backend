package com.halo.eventer.domain.parking.controller;

import java.util.List;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.halo.eventer.domain.parking.api_docs.ParkingNoticeDoc;
import com.halo.eventer.domain.parking.dto.common.VisibilityChangeReqDto;
import com.halo.eventer.domain.parking.dto.parking_notice.ParkingNoticeContentReqDto;
import com.halo.eventer.domain.parking.dto.parking_notice.ParkingNoticeReqDto;
import com.halo.eventer.domain.parking.dto.parking_notice.ParkingNoticeResDto;
import com.halo.eventer.domain.parking.service.ParkingNoticeService;
import com.halo.eventer.global.config.ControllerTestSecurityBeans;
import com.halo.eventer.global.config.security.SecurityConfig;
import com.halo.eventer.global.security.provider.JwtProvider;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ParkingNoticeController.class)
@AutoConfigureRestDocs
@ActiveProfiles("test")
@SuppressWarnings("NonAsciiCharacters")
@Import({ControllerTestSecurityBeans.class, SecurityConfig.class})
class ParkingNoticeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    ParkingNoticeService parkingNoticeService;

    @MockitoBean
    JwtProvider jwtProvider;

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 관리자_공지_생성_성공() throws Exception {
        // given
        Long parkingManagementId = 1L;
        ParkingNoticeReqDto dto = new ParkingNoticeReqDto("제목", "내용");

        // when // then
        mockMvc.perform(post(
                                "/api/v2/admin/parking-managements/{parkingManagementId}/parking-notices",
                                parkingManagementId)
                        .header("Authorization", "Bearer test-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andDo(ParkingNoticeDoc.create());

        verify(parkingNoticeService).create(eq(parkingManagementId), any(ParkingNoticeReqDto.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 관리자_공지_생성_실패_title_null_검증에러() throws Exception {
        Long parkingManagementId = 1L;
        String body = """
                {"title":null,"content":"내용","visible":true}
                """;

        mockMvc.perform(post(
                                "/api/v2/admin/parking-managements/{parkingManagementId}/parking-notices",
                                parkingManagementId)
                        .header("Authorization", "Bearer test-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andDo(ParkingNoticeDoc.errorSnippet("관리자 공지 생성시 title null 불가"));

        verify(parkingNoticeService, never()).create(anyLong(), any());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 관리자_공지_목록조회_성공() throws Exception {
        Long parkingManagementId = 1L;
        List<ParkingNoticeResDto> list = List.of(
                new ParkingNoticeResDto(10L, "t1", "c1", true), new ParkingNoticeResDto(20L, "t2", "c2", false));
        when(parkingNoticeService.getParkingNotices(parkingManagementId)).thenReturn(list);

        mockMvc.perform(get(
                                "/api/v2/admin/parking-managements/{parkingManagementId}/parking-notices",
                                parkingManagementId)
                        .header("Authorization", "Bearer test-token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(10))
                .andExpect(jsonPath("$[0].title").value("t1"))
                .andExpect(jsonPath("$[0].content").value("c1"))
                .andExpect(jsonPath("$[0].visible").value(true))
                .andExpect(jsonPath("$[1].id").value(20))
                .andExpect(jsonPath("$[1].visible").value(false))
                .andDo(ParkingNoticeDoc.getAdminList());

        verify(parkingNoticeService).getParkingNotices(parkingManagementId);
    }

    @Test
    void 사용자_visible_공지_조회_성공() throws Exception {
        Long id = 7L;
        List<ParkingNoticeResDto> list = List.of(new ParkingNoticeResDto(1L, "알림", "본문", true));
        when(parkingNoticeService.getVisibleParkingNotices(id)).thenReturn(list);

        mockMvc.perform(get("/api/v2/user/parking-managements/{parkingManagementId}/parking-notices", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].visible").value(true))
                .andDo(ParkingNoticeDoc.getUserVisibleList());

        verify(parkingNoticeService).getVisibleParkingNotices(id);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 공지_내용수정_성공() throws Exception {
        Long id = 11L;
        ParkingNoticeContentReqDto dto = new ParkingNoticeContentReqDto("수정제목", "수정내용");

        mockMvc.perform(patch("/api/v2/admin/parking-notices/{id}/content", id)
                        .header("Authorization", "Bearer test-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andDo(ParkingNoticeDoc.updateContent());

        verify(parkingNoticeService).updateContent(eq(id), any(ParkingNoticeContentReqDto.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 공지_내용수정_실패_title_null_검증에러() throws Exception {
        Long id = 11L;
        String body = """
                {"title":null,"content":"수정내용"}
                """;

        mockMvc.perform(patch("/api/v2/admin/parking-notices/{id}/content", id)
                        .header("Authorization", "Bearer test-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andDo(ParkingNoticeDoc.errorSnippet("공지 내용 수정시 title null 불가"));

        verify(parkingNoticeService, never()).updateContent(anyLong(), any());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 공지_노출여부_변경_성공() throws Exception {
        Long id = 100L;
        VisibilityChangeReqDto dto = new VisibilityChangeReqDto(false);

        mockMvc.perform(patch("/api/v2/admin/parking-notices/{id}/visibility", id)
                        .header("Authorization", "Bearer test-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andDo(ParkingNoticeDoc.changeVisibility());

        verify(parkingNoticeService).changeVisibility(eq(id), any(VisibilityChangeReqDto.class));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 공지_노출여부_변경_실패_visible_null_검증에러() throws Exception {
        Long id = 100L;
        String body = """
                {"visible":null}
                """;

        mockMvc.perform(patch("/api/v2/admin/parking-notices/{id}/visibility", id)
                        .header("Authorization", "Bearer test-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andDo(ParkingNoticeDoc.errorSnippet("공지 노출 여부 null 불가"));

        verify(parkingNoticeService, never()).changeVisibility(anyLong(), any());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 공지_삭제_성공() throws Exception {
        Long id = 55L;

        mockMvc.perform(delete("/api/v2/admin/parking-notices/{id}", id)
                        .header("Authorization", "Bearer test-token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(ParkingNoticeDoc.deleteNotice());

        verify(parkingNoticeService).delete(id);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 경로변수_검증_실패_parkingManagementId_0이면() throws Exception {
        mockMvc.perform(get("/api/v2/admin/parking-managements/{parkingManagementId}/parking-notices", 0L)
                        .header("Authorization", "Bearer test-token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(ParkingNoticeDoc.errorSnippet("어드민 조회시 id 1 미만 오류"));
        ;

        verify(parkingNoticeService, never()).getParkingNotices(anyLong());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 경로변수_검증_실패_id_0이면() throws Exception {
        mockMvc.perform(delete("/api/v2/admin/parking-notices/{id}", 0L)
                        .header("Authorization", "Bearer test-token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(parkingNoticeService, never()).delete(anyLong());
    }

    @Test
    void 관리자_공지_생성_인증없음_401() throws Exception {
        Long parkingManagementId = 1L;
        String body = """
                {"title":"t","content":"c","visible":true}
                """;

        mockMvc.perform(post(
                                "/api/v2/admin/parking-managements/{parkingManagementId}/parking-notices",
                                parkingManagementId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isUnauthorized())
                .andDo(ParkingNoticeDoc.errorSnippet("주차장 공지 생성 인증 거부"));

        verify(parkingNoticeService, never()).create(anyLong(), any());
    }

    @Test
    void 관리자_공지_목록조회_인증없음_401() throws Exception {
        mockMvc.perform(get("/api/v2/admin/parking-managements/{parkingManagementId}/parking-notices", 1L))
                .andExpect(status().isUnauthorized())
                .andDo(ParkingNoticeDoc.errorSnippet("어드민용 주차장 공지 목록 조회 인증 거부"));

        verify(parkingNoticeService, never()).getParkingNotices(anyLong());
    }

    @Test
    void 공지_내용수정_인증없음_401() throws Exception {
        Long id = 11L;
        String body = """
                {"title":"t","content":"c"}
                """;

        mockMvc.perform(patch("/api/v2/admin/parking-notices/{id}/content", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isUnauthorized())
                .andDo(ParkingNoticeDoc.errorSnippet("주차장 공지 내용 수정 인증거부"));
        verify(parkingNoticeService, never()).updateContent(anyLong(), any());
    }

    @Test
    void 공지_노출여부_변경_인증없음_401() throws Exception {
        Long id = 100L;
        String body = """
                {"visible":false}
                """;

        mockMvc.perform(patch("/api/v2/admin/parking-notices/{id}/visibility", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isUnauthorized())
                .andDo(ParkingNoticeDoc.errorSnippet("주차장 공지 visible 변경 인증 거부"));

        verify(parkingNoticeService, never()).changeVisibility(anyLong(), any());
    }

    @Test
    void 공지_삭제_인증없음_401() throws Exception {
        mockMvc.perform(delete("/api/v2/admin/parking-notices/{id}", 55L))
                .andExpect(status().isUnauthorized())
                .andDo(ParkingNoticeDoc.errorSnippet("주차장 공지 삭제 인증 거부"));

        verify(parkingNoticeService, never()).delete(anyLong());
    }
}
