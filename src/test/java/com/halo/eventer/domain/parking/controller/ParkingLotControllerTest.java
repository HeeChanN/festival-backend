package com.halo.eventer.domain.parking.controller;

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
import com.halo.eventer.domain.parking.api_docs.ParkingLotDoc;
import com.halo.eventer.domain.parking.dto.common.VisibilityChangeReqDto;
import com.halo.eventer.domain.parking.dto.parking_lot.AdminParkingLotResDto;
import com.halo.eventer.domain.parking.dto.parking_lot.CongestionLevelReqDto;
import com.halo.eventer.domain.parking.dto.parking_lot.ParkingLotReqDto;
import com.halo.eventer.domain.parking.service.ParkingLotService;
import com.halo.eventer.global.config.ControllerTestSecurityBeans;
import com.halo.eventer.global.config.security.SecurityConfig;
import com.halo.eventer.global.security.provider.JwtProvider;

import static com.halo.eventer.global.common.ApiErrorAssert.assertError;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ParkingLotController.class)
@AutoConfigureRestDocs
@SuppressWarnings("NonAsciiCharacters")
@ActiveProfiles("test")
@Import({ControllerTestSecurityBeans.class, SecurityConfig.class})
public class ParkingLotControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ParkingLotService parkingLotService;

    @MockitoBean
    private JwtProvider jwtProvider;

    private ParkingLotReqDto createReq;
    private ParkingLotReqDto updateReq;
    private AdminParkingLotResDto adminRes;
    private VisibilityChangeReqDto visibleOnReq;
    private CongestionLevelReqDto congestionReq;

    @BeforeEach
    void setUp() {
        createReq = new ParkingLotReqDto("제1주차장", "서울특별시", "강남구", "역삼동", "테헤란로", "427", "멀티캠퍼스", 37.501, 127.036);

        updateReq = new ParkingLotReqDto("제1주차장(수정)", "서울특별시", "강남구", "역삼동", "테헤란로", "427", "멀티캠퍼스", 37.502, 127.037);

        adminRes =
                new AdminParkingLotResDto(1L, "제1주차장", "서울특별시", "강남구", "역삼동", "테헤란로", "427", "멀티캠퍼스", 37.501, 127.036);

        visibleOnReq = new VisibilityChangeReqDto(true);

        congestionReq = new CongestionLevelReqDto("HIGH");
    }

    // ----------------------------------------
    // 생성
    // ----------------------------------------
    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 주차면_생성_성공() throws Exception {
        // given
        doNothing().when(parkingLotService).create(eq(10L), any(ParkingLotReqDto.class));

        // when & then
        mockMvc.perform(post("/api/v2/admin/parking-zones/{parkingZoneId}/parking-lots", 10L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq)))
                .andExpect(status().isOk())
                .andDo(ParkingLotDoc.create());
    }

    @Test
    void 주차면_생성_인증거부() throws Exception {
        ResultActions result = mockMvc.perform(post("/api/v2/admin/parking-zones/{parkingZoneId}/parking-lots", 10L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq)))
                .andExpect(status().isUnauthorized())
                .andDo(ParkingLotDoc.errorSnippet("주차_생성_인증_거부"));
        assertError(result, "A002", "Unauthenticated", 401);
    }

    // ----------------------------------------
    // 단일 조회
    // ----------------------------------------
    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 주차면_단일조회_성공() throws Exception {
        // given
        given(parkingLotService.getParkingLot(1L)).willReturn(adminRes);

        // when & then
        mockMvc.perform(get("/api/v2/admin/parking-lots/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(adminRes)))
                .andDo(ParkingLotDoc.get());
    }

    @Test
    void 주차면_단일조회_인증거부() throws Exception {
        ResultActions result = mockMvc.perform(get("/api/v2/admin/parking-lots/{id}", 1L))
                .andExpect(status().isUnauthorized())
                .andDo(ParkingLotDoc.errorSnippet("주차_조회_인증_거부"));
        assertError(result, "A002", "Unauthenticated", 401);
    }

    // ----------------------------------------
    // 가시성 변경
    // ----------------------------------------
    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 주차면_가시성_변경_성공() throws Exception {
        // given
        doNothing().when(parkingLotService).changeVisible(eq(1L), any(VisibilityChangeReqDto.class));

        // when & then
        mockMvc.perform(patch("/api/v2/admin/parking-lots/{id}/visibility", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(visibleOnReq)))
                .andExpect(status().isOk())
                .andDo(ParkingLotDoc.changeVisibility());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 주차면_가시성_변경시_visible_null_검증실패() throws Exception {
        // given
        VisibilityChangeReqDto bad = new VisibilityChangeReqDto(null);

        // when & then
        ResultActions result = mockMvc.perform(patch("/api/v2/admin/parking-lots/{id}/visibility", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bad)))
                .andExpect(status().isBadRequest())
                .andDo(ParkingLotDoc.errorSnippet("주차_가시성_변경_검증실패_visible_null"));
        // 프로젝트 규칙에 맞춘 에러 코드/메시지
        assertError(result, "C013", "must not be null", 400);
    }

    @Test
    void 주차면_가시성_변경_인증거부() throws Exception {
        ResultActions result = mockMvc.perform(patch("/api/v2/admin/parking-lots/{id}/visibility", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(visibleOnReq)))
                .andExpect(status().isUnauthorized())
                .andDo(ParkingLotDoc.errorSnippet("주차_가시성_변경_인증_거부"));
        assertError(result, "A002", "Unauthenticated", 401);
    }

    // ----------------------------------------
    // 내용 수정
    // ----------------------------------------
    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 주차면_내용_수정_성공() throws Exception {
        // given
        doNothing().when(parkingLotService).updateContent(eq(1L), any(ParkingLotReqDto.class));

        // when & then
        mockMvc.perform(put("/api/v2/admin/parking-lots/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk())
                .andDo(ParkingLotDoc.updateContent());
    }

    @Test
    void 주차면_내용_수정_인증거부() throws Exception {
        ResultActions result = mockMvc.perform(put("/api/v2/admin/parking-lots/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isUnauthorized())
                .andDo(ParkingLotDoc.errorSnippet("주차_내용_수정_인증_거부"));
        assertError(result, "A002", "Unauthenticated", 401);
    }

    // ----------------------------------------
    // 혼잡도 변경
    // ----------------------------------------
    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 주차면_혼잡도_변경_성공() throws Exception {
        // given
        doNothing().when(parkingLotService).changeCongestionLevel(eq(1L), any(CongestionLevelReqDto.class));

        // when & then
        mockMvc.perform(patch("/api/v2/admin/parking-lots/{id}/congestion-level", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(congestionReq)))
                .andExpect(status().isOk())
                .andDo(ParkingLotDoc.changeCongestionLevel());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 주차면_혼잡도_변경시_레벨_null_검증실패() throws Exception {
        // given
        CongestionLevelReqDto bad = new CongestionLevelReqDto(null);

        // when & then
        ResultActions result = mockMvc.perform(patch("/api/v2/admin/parking-lots/{id}/congestion-level", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bad)))
                .andExpect(status().isBadRequest())
                .andDo(ParkingLotDoc.errorSnippet("주차_혼잡도_변경_검증실패_level_null"));
        // 프로젝트 규칙에 맞춘 에러 코드/메시지
        assertError(result, "C013", "must not be null", 400);
    }

    @Test
    void 주차면_혼잡도_변경_인증거부() throws Exception {
        ResultActions result = mockMvc.perform(patch("/api/v2/admin/parking-lots/{id}/congestion-level", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(congestionReq)))
                .andExpect(status().isUnauthorized())
                .andDo(ParkingLotDoc.errorSnippet("주차_혼잡도_변경_인증_거부"));
        assertError(result, "A002", "Unauthenticated", 401);
    }

    // ----------------------------------------
    // 삭제
    // ----------------------------------------
    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 주차면_삭제_성공() throws Exception {
        // given
        doNothing().when(parkingLotService).delete(1L);

        // when & then
        mockMvc.perform(delete("/api/v2/admin/parking-lots/{id}", 1L))
                .andExpect(status().isOk())
                .andDo(ParkingLotDoc.delete());
        verify(parkingLotService, times(1)).delete(1L);
    }

    @Test
    void 주차_삭제_인증거부() throws Exception {
        ResultActions result = mockMvc.perform(delete("/api/v2/admin/parking-lots/{id}", 1L))
                .andExpect(status().isUnauthorized())
                .andDo(ParkingLotDoc.errorSnippet("주차_삭제_인증_거부"));
        assertError(result, "A002", "Unauthenticated", 401);
    }
}
