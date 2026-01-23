package com.halo.eventer.domain.parking.controller;

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
import com.halo.eventer.domain.parking.api_docs.ParkingZoneDoc;
import com.halo.eventer.domain.parking.dto.common.DisplayOrderChangeReqDto;
import com.halo.eventer.domain.parking.dto.common.NameUpdateReqDto;
import com.halo.eventer.domain.parking.dto.common.VisibilityChangeReqDto;
import com.halo.eventer.domain.parking.dto.parking_zone.ParkingZoneReqDto;
import com.halo.eventer.domain.parking.dto.parking_zone.ParkingZoneResDto;
import com.halo.eventer.domain.parking.service.ParkingZoneService;
import com.halo.eventer.global.config.ControllerTestSecurityBeans;
import com.halo.eventer.global.config.security.SecurityConfig;
import com.halo.eventer.global.security.provider.JwtProvider;

import static com.halo.eventer.global.common.ApiErrorAssert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ParkingZoneController.class)
@AutoConfigureRestDocs
@SuppressWarnings("NonAsciiCharacters")
@ActiveProfiles("test")
@Import({ControllerTestSecurityBeans.class, SecurityConfig.class})
public class ParkingZoneControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ParkingZoneService parkingZoneService;

    @MockitoBean
    private JwtProvider jwtProvider;

    private ParkingZoneReqDto createReq;
    private ParkingZoneResDto zone1;
    private ParkingZoneResDto zone2;

    private VisibilityChangeReqDto visibilityOnReq;
    private NameUpdateReqDto renameReq;
    private DisplayOrderChangeReqDto displayOrderReq;

    @BeforeEach
    void setUp() {
        createReq = new ParkingZoneReqDto();
        // @NoArgsConstructor + 필드 접근자만 있으므로, 직렬화 테스트 편의상 JSON 문자열로 바인딩할 예정

        visibilityOnReq = new VisibilityChangeReqDto(true);
        renameReq = new NameUpdateReqDto();
        displayOrderReq = new DisplayOrderChangeReqDto();

        // 응답 DTO fixture
        var lot1 = new com.halo.eventer.domain.parking.dto.parking_lot.ParkingLotSummaryDto(
                100L, "A구역 1번", "LOW", true, "서울 강남구...", "강남구 ...", 0, 0);
        var lot2 = new com.halo.eventer.domain.parking.dto.parking_lot.ParkingLotSummaryDto(
                101L, "A구역 2번", "HIGH", true, "서울 강남구...", "강남구 ...", 0, 0);

        zone1 = new ParkingZoneResDto(1L, "A구역", true, List.of(lot1, lot2));
        zone2 = new ParkingZoneResDto(2L, "B구역", false, List.of());
    }

    // -----------------------------
    // 생성
    // -----------------------------
    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 주차구역_생성_성공() throws Exception {
        // given
        doNothing().when(parkingZoneService).create(eq(10L), any(ParkingZoneReqDto.class));

        // when & then
        mockMvc.perform(
                        post("/api/v2/admin/parking-managements/{parkingManagementId}/parking-zones", 10L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                                { "name": "A구역", "visible": true }
                                """))
                .andExpect(status().isOk())
                .andDo(ParkingZoneDoc.create());
    }

    @Test
    void 주차구역_생성_인증거부() throws Exception {
        ResultActions result = mockMvc.perform(
                        post("/api/v2/admin/parking-managements/{parkingManagementId}/parking-zones", 10L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                                { "name": "A구역", "visible": true }
                                """))
                .andExpect(status().isUnauthorized())
                .andDo(ParkingZoneDoc.errorSnippet("주차구역_생성_인증_거부"));
        assertError(result, "A002", "Unauthenticated", 401);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 주차구역_생성시_name_빈값이면_400() throws Exception {
        ResultActions result = mockMvc.perform(
                        post("/api/v2/admin/parking-managements/{parkingManagementId}/parking-zones", 10L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                                { "name": "", "visible": true }
                                """))
                .andExpect(status().isBadRequest())
                .andDo(ParkingZoneDoc.errorSnippet("주차구역_생성_name_NotEmpty"));
        assertError(result, "C013", "must not be empty", 400);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 주차구역_생성시_visible_null이면_400() throws Exception {
        ResultActions result = mockMvc.perform(
                        post("/api/v2/admin/parking-managements/{parkingManagementId}/parking-zones", 10L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                                { "name": "A구역", "visible": null }
                                """))
                .andExpect(status().isBadRequest())
                .andDo(ParkingZoneDoc.errorSnippet("주차구역_생성_visible_NotNull"));
        assertError(result, "C013", "must not be null", 400);
    }

    // -----------------------------
    // 관리자용 목록 조회
    // -----------------------------
    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 주차구역_목록_조회_성공_ADMIN() throws Exception {
        // given
        given(parkingZoneService.getParkingZones(10L)).willReturn(List.of(zone1, zone2));

        // when & then
        mockMvc.perform(get("/api/v2/admin/parking-managements/{parkingManagementId}/parking-zones", 10L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(zone1.getId()))
                .andExpect(jsonPath("$[0].name").value(zone1.getName()))
                .andExpect(jsonPath("$[0].visible").value(zone1.getVisible()))
                .andExpect(jsonPath("$[0].parkingLots.length()")
                        .value(zone1.getParkingLots().size()))
                .andDo(ParkingZoneDoc.getAdminList());
    }

    @Test
    void 주차구역_목록_조회_인증거부_ADMIN() throws Exception {
        ResultActions result = mockMvc.perform(
                        get("/api/v2/admin/parking-managements/{parkingManagementId}/parking-zones", 10L))
                .andExpect(status().isUnauthorized())
                .andDo(ParkingZoneDoc.errorSnippet("주차구역_목록_ADMIN_인증_거부"));
        assertError(result, "A002", "Unauthenticated", 401);
    }

    // -----------------------------
    // 사용자용 목록 조회(공개)
    // -----------------------------
    @Test
    void 주차구역_목록_조회_USER_공개목록() throws Exception {
        // given
        given(parkingZoneService.getVisibleParkingZones(10L)).willReturn(List.of(zone1));

        // when & then
        mockMvc.perform(get("/api/v2/user/parking-managements/{parkingManagementId}/parking-zones", 10L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(zone1.getId()))
                .andExpect(jsonPath("$[0].visible").value(true))
                .andDo(ParkingZoneDoc.getUserList());
    }

    // -----------------------------
    // 가시성 변경
    // -----------------------------
    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 주차구역_가시성_변경_성공() throws Exception {
        doNothing().when(parkingZoneService).changeVisible(eq(1L), any(VisibilityChangeReqDto.class));

        mockMvc.perform(
                        patch("/api/v2/admin/parking-zones/{id}/visibility", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                                { "visible": true }
                                """))
                .andExpect(status().isOk())
                .andDo(ParkingZoneDoc.changeVisibility());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 주차구역_가시성_변경시_visible_null이면_400() throws Exception {
        ResultActions result = mockMvc.perform(
                        patch("/api/v2/admin/parking-zones/{id}/visibility", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                                { "visible": null }
                                """))
                .andExpect(status().isBadRequest())
                .andDo(ParkingZoneDoc.errorSnippet("주차구역_가시성_변경_visible_NotNull"));
        assertError(result, "C013", "must not be null", 400);
    }

    @Test
    void 주차구역_가시성_변경_인증거부() throws Exception {
        ResultActions result = mockMvc.perform(
                        patch("/api/v2/admin/parking-zones/{id}/visibility", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                                { "visible": true }
                                """))
                .andExpect(status().isUnauthorized())
                .andDo(ParkingZoneDoc.errorSnippet("주차구역_가시성_변경_인증_거부"));
        assertError(result, "A002", "Unauthenticated", 401);
    }

    // -----------------------------
    // 이름 변경
    // -----------------------------
    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 주차구역_이름_변경_성공() throws Exception {
        doNothing().when(parkingZoneService).changeName(eq(1L), any(NameUpdateReqDto.class));

        mockMvc.perform(
                        patch("/api/v2/admin/parking-zones/{id}/name", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                                { "name": "B구역" }
                                """))
                .andExpect(status().isOk())
                .andDo(ParkingZoneDoc.rename());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 주차구역_이름_변경시_name_빈값이면_400() throws Exception {
        ResultActions result = mockMvc.perform(patch("/api/v2/admin/parking-zones/{id}/name", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(
                                """
                                { "name": "" }
                                """))
                .andExpect(status().isBadRequest())
                .andDo(ParkingZoneDoc.errorSnippet("주차구역_이름_변경_name_NotEmpty"));
        assertError(result, "C013", "must not be empty", 400);
    }

    @Test
    void 주차구역_이름_변경_인증거부() throws Exception {
        ResultActions result = mockMvc.perform(
                        patch("/api/v2/admin/parking-zones/{id}/name", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                                { "name": "B구역" }
                                """))
                .andExpect(status().isUnauthorized())
                .andDo(ParkingZoneDoc.errorSnippet("주차구역_이름_변경_인증_거부"));
        assertError(result, "A002", "Unauthenticated", 401);
    }

    // -----------------------------
    // 표시 순서 변경
    // -----------------------------
    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 주차구역_표시순서_변경_성공() throws Exception {
        doNothing().when(parkingZoneService).changeDisplayOrder(eq(10L), any(DisplayOrderChangeReqDto.class));

        mockMvc.perform(
                        put("/api/v2/admin/parking-managements/{parkingManagementId}/parking-zones/display-order", 10L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                                { "ids": [2,1,3] }
                                """))
                .andExpect(status().isOk())
                .andDo(ParkingZoneDoc.changeDisplayOrder());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 주차구역_표시순서_변경시_ids_null이면_400() throws Exception {
        ResultActions result = mockMvc.perform(
                        put("/api/v2/admin/parking-managements/{parkingManagementId}/parking-zones/display-order", 10L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                                { "ids": null }
                                """))
                .andExpect(status().isBadRequest())
                .andDo(ParkingZoneDoc.errorSnippet("주차구역_표시순서_ids_NotNull"));
        assertError(result, "C013", "must not be null", 400);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 주차구역_표시순서_변경시_ids에_0_포함되면_400() throws Exception {
        ResultActions result = mockMvc.perform(
                        put("/api/v2/admin/parking-managements/{parkingManagementId}/parking-zones/display-order", 10L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                                { "ids": [1,0,3] }
                                """))
                .andExpect(status().isBadRequest())
                .andDo(ParkingZoneDoc.errorSnippet("주차구역_표시순서_ids_Min"));
        assertError(result, "C013", "must be greater than or equal to 1", 400);
    }

    @Test
    void 주차구역_표시순서_변경_인증거부() throws Exception {
        ResultActions result = mockMvc.perform(
                        put("/api/v2/admin/parking-managements/{parkingManagementId}/parking-zones/display-order", 10L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                                { "ids": [2,1,3] }
                                """))
                .andExpect(status().isUnauthorized())
                .andDo(ParkingZoneDoc.errorSnippet("주차구역_표시순서_인증_거부"));
        assertError(result, "A002", "Unauthenticated", 401);
    }

    // -----------------------------
    // 삭제
    // -----------------------------
    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 주차구역_삭제_성공() throws Exception {
        doNothing().when(parkingZoneService).delete(10L, 1L);

        mockMvc.perform(delete("/api/v2/admin/parking-managements/{parkingManagementId}/parking-zones/{id}", 10L, 1L))
                .andExpect(status().isOk())
                .andDo(ParkingZoneDoc.delete());
        verify(parkingZoneService, times(1)).delete(10L, 1L);
    }

    @Test
    void 주차구역_삭제_인증거부() throws Exception {
        ResultActions result = mockMvc.perform(
                        delete("/api/v2/admin/parking-managements/{parkingManagementId}/parking-zones/{id}", 10L, 1L))
                .andExpect(status().isUnauthorized())
                .andDo(ParkingZoneDoc.errorSnippet("주차구역_삭제_인증_거부"));
        assertError(result, "A002", "Unauthenticated", 401);
    }
}
