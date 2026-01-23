package com.halo.eventer.domain.parking.controller;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.halo.eventer.domain.image.dto.FileDto;
import com.halo.eventer.domain.image.dto.ImageDto;
import com.halo.eventer.domain.parking.api_docs.ParkingManagementDoc;
import com.halo.eventer.domain.parking.dto.common.DisplayOrderChangeReqDto;
import com.halo.eventer.domain.parking.dto.parking_management.ParkingManagementReqDto;
import com.halo.eventer.domain.parking.dto.parking_management.ParkingManagementResDto;
import com.halo.eventer.domain.parking.dto.parking_management.ParkingManagementSubPageResDto;
import com.halo.eventer.domain.parking.dto.parking_management.ParkingSubPageReqDto;
import com.halo.eventer.domain.parking.service.ParkingManagementService;
import com.halo.eventer.global.config.ControllerTestSecurityBeans;
import com.halo.eventer.global.config.security.SecurityConfig;
import com.halo.eventer.global.security.provider.JwtProvider;

import static com.halo.eventer.global.common.ApiErrorAssert.assertError;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ParkingManagementController.class)
@AutoConfigureRestDocs
@ActiveProfiles("test")
@SuppressWarnings("NonAsciiCharacters")
@Import({ControllerTestSecurityBeans.class, SecurityConfig.class})
class ParkingManagementControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    ParkingManagementService parkingManagementService;

    @MockitoBean
    JwtProvider jwtProvider;

    private static final String ADMIN_TOKEN = "Bearer admin-token";
    private static final long VALID_ID = 1L;
    private static final long INVALID_ID = 0L;

    private Map<String, Object> validCreateBody;

    @BeforeEach
    void setUp() {
        // DTO 실제 필드에 맞춘 요청 본문
        validCreateBody = Map.of(
                "headerName", "주차 안내",
                "parkingInfoType", "BASIC", // Pattern: BASIC|BUTTON|SIMPLE
                "title", "캠퍼스 주차 정보",
                "description", "주차장 위치와 요금 안내",
                "buttonName", "자세히 보기",
                "buttonTargetUrl", "https://example.com/parking",
                "backgroundImage", "https://cdn/bg.png",
                "visible", true);
    }

    // ======================= ADMIN =======================
    @Nested
    @WithMockUser(username = "admin", roles = "ADMIN")
    class 어드민_엔드포인트 {

        @Test
        void 생성_성공() throws Exception {
            doNothing().when(parkingManagementService).create(eq(VALID_ID), any(ParkingManagementReqDto.class));

            mockMvc.perform(post("/api/v2/admin/festivals/{festivalId}/parking-managements", VALID_ID)
                            .header("Authorization", ADMIN_TOKEN)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validCreateBody)))
                    .andExpect(status().isOk())
                    .andDo(ParkingManagementDoc.create());
        }

        @Test
        void 생성_실패_festivalId_Min_검증() throws Exception {
            ResultActions result = mockMvc.perform(
                            post("/api/v2/admin/festivals/{festivalId}/parking-managements", INVALID_ID)
                                    .header("Authorization", ADMIN_TOKEN)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(validCreateBody)))
                    .andExpect(status().isBadRequest())
                    .andDo(ParkingManagementDoc.errorSnippet("주차 관리 시스템 생성 festivalId 검증 실패"));
            assertError(result, "C013", "must be greater than or equal to 1", 400);
        }

        @Test
        void 생성_실패_parkingInfoType_Pattern_검증() throws Exception {
            Map<String, Object> badRequest = Map.of(
                    "headerName", "주차 안내",
                    "parkingInfoType", "INVALID",
                    "title", "캠퍼스 주차 정보",
                    "description", "주차장 위치와 요금 안내",
                    "buttonName", "자세히 보기",
                    "buttonTargetUrl", "https://example.com/parking",
                    "backgroundImage", "https://cdn/bg.png",
                    "visible", true);

            ResultActions result = mockMvc.perform(
                            post("/api/v2/admin/festivals/{festivalId}/parking-managements", VALID_ID)
                                    .header("Authorization", ADMIN_TOKEN)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(badRequest)))
                    .andExpect(status().isBadRequest())
                    .andDo(ParkingManagementDoc.errorSnippet("주차 관리 시스템 생성시 parkingInfoType 패턴 오류"));
            assertError(result, "C013", "parkingInfoType must be one of: BASIC, BUTTON, SIMPLE", 400);
        }

        @Test
        void 수정_성공() throws Exception {
            doNothing().when(parkingManagementService).update(eq(VALID_ID), any(ParkingManagementReqDto.class));

            mockMvc.perform(put("/api/v2/admin/parking-managements/{id}", VALID_ID)
                            .header("Authorization", ADMIN_TOKEN)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validCreateBody)))
                    .andExpect(status().isOk())
                    .andDo(ParkingManagementDoc.update());
        }

        @Test
        void 수정_실패_id_Min_검증() throws Exception {
            ResultActions result = mockMvc.perform(put("/api/v2/admin/parking-managements/{id}", INVALID_ID)
                            .header("Authorization", ADMIN_TOKEN)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validCreateBody)))
                    .andExpect(status().isBadRequest())
                    .andDo(ParkingManagementDoc.errorSnippet("주차 관리 시스템 수정시 id 값 오류"));
            assertError(result, "C013", "must be greater than or equal to 1", 400);
        }

        @Test
        void 서브페이지_헤더명_수정_성공() throws Exception {
            doNothing()
                    .when(parkingManagementService)
                    .updateSubPageHeaderName(eq(VALID_ID), any(ParkingSubPageReqDto.class));

            Map<String, Object> body = Map.of("subPageHeaderName", "주차구역 안내");
            mockMvc.perform(patch("/api/v2/admin/parking-managements/{id}/sub-page-header-name", VALID_ID)
                            .header("Authorization", ADMIN_TOKEN)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body)))
                    .andExpect(status().isOk())
                    .andDo(ParkingManagementDoc.updateSubPageHeaderName());
        }

        @Test
        void 서브페이지_헤더명_수정_실패_id_Min_검증() throws Exception {
            ParkingSubPageReqDto body = new ParkingSubPageReqDto("주차 구역안내");
            ResultActions result = mockMvc.perform(
                            patch("/api/v2/admin/parking-managements/{id}/sub-page-header-name", INVALID_ID)
                                    .header("Authorization", ADMIN_TOKEN)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(body)))
                    .andExpect(status().isBadRequest())
                    .andDo(ParkingManagementDoc.errorSnippet("서브페이지 헤더명 수정시 id 오류"));
            assertError(result, "C013", "must be greater than or equal to 1", 400);
        }

        @Test
        void 서브페이지_헤더명_수정_실패_필드_NotNull() throws Exception {
            ParkingSubPageReqDto body = new ParkingSubPageReqDto();

            ResultActions result = mockMvc.perform(
                            patch("/api/v2/admin/parking-managements/{id}/sub-page-header-name", VALID_ID)
                                    .header("Authorization", ADMIN_TOKEN)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(body)))
                    .andExpect(status().isBadRequest())
                    .andDo(ParkingManagementDoc.errorSnippet("서브페이지 헤더명 Null일 경우"));
            assertError(result, "C013", "must not be null", 400);
        }

        @Test
        void 주차맵이미지_등록_성공() throws Exception {
            doNothing().when(parkingManagementService).createParkingMapImage(eq(VALID_ID), any(FileDto.class));
            FileDto fileDto = new FileDto("url");

            mockMvc.perform(post("/api/v2/admin/parking-managements/{id}/parking-map-images", VALID_ID)
                            .header("Authorization", ADMIN_TOKEN)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(fileDto)))
                    .andExpect(status().isOk())
                    .andDo(ParkingManagementDoc.createParkingMapImage());
        }

        @Test
        void 주차맵이미지_등록_실패_id_Min_검증() throws Exception {
            FileDto fileDto = new FileDto("url");

            ResultActions result = mockMvc.perform(
                            post("/api/v2/admin/parking-managements/{id}/parking-map-images", INVALID_ID)
                                    .header("Authorization", ADMIN_TOKEN)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(fileDto)))
                    .andExpect(status().isBadRequest())
                    .andDo(ParkingManagementDoc.errorSnippet("주차맵이미지_등록시_id_오류"));
            assertError(result, "C013", "must be greater than or equal to 1", 400);
        }

        @Test
        void 주차맵이미지_정렬변경_성공() throws Exception {
            doNothing()
                    .when(parkingManagementService)
                    .updateParkingMapImageDisplayOrder(eq(VALID_ID), any(DisplayOrderChangeReqDto.class));

            Map<String, Object> body = Map.of("ids", List.of(3L, 1L, 2L));
            mockMvc.perform(patch("/api/v2/admin/parking-managements/{id}/parking-map-images/display-order", VALID_ID)
                            .header("Authorization", ADMIN_TOKEN)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body)))
                    .andExpect(status().isOk())
                    .andDo(ParkingManagementDoc.updateParkingMapImageDisplayOrder());
        }

        @Test
        void 주차맵이미지_정렬변경_실패_id_Min_검증() throws Exception {
            Map<String, Object> body = Map.of("imageIds", List.of(3L, 1L, 0L));
            ResultActions result = mockMvc.perform(
                            patch("/api/v2/admin/parking-managements/{id}/parking-map-images/display-order", INVALID_ID)
                                    .header("Authorization", ADMIN_TOKEN)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(body)))
                    .andExpect(status().isBadRequest())
                    .andDo(ParkingManagementDoc.errorSnippet("주차맵이미지 순서변경시 id 오류"));
            assertError(result, "C013", "must be greater than or equal to 1", 400);
        }

        @Test
        void 주차맵이미지_삭제_성공() throws Exception {
            doNothing()
                    .when(parkingManagementService)
                    .deleteParkingMapImages(eq(VALID_ID), any(DisplayOrderChangeReqDto.class));

            Map<String, Object> body = Map.of("ids", List.of(10L, 11L));
            mockMvc.perform(delete("/api/v2/admin/parking-managements/{id}/parking-map-images", VALID_ID)
                            .header("Authorization", ADMIN_TOKEN)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(body)))
                    .andExpect(status().isOk())
                    .andDo(ParkingManagementDoc.deleteParkingMapImages());
        }

        @Test
        void 주차맵이미지_삭제_실패_id_Min_검증() throws Exception {
            Map<String, Object> body = Map.of("imageIds", List.of(0L, 11L));
            ResultActions result = mockMvc.perform(
                            delete("/api/v2/admin/parking-managements/{id}/parking-map-images", INVALID_ID)
                                    .header("Authorization", ADMIN_TOKEN)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(body)))
                    .andExpect(status().isBadRequest())
                    .andDo(ParkingManagementDoc.errorSnippet("주차맵이미지 삭제시 id 오류"));
            assertError(result, "C013", "must be greater than or equal to 1", 400);
        }

        @Test
        void 주차맵이미지_삭제_실패_imageIds_NotNull() throws Exception {
            Map<String, Object> body = Map.of();
            ResultActions result = mockMvc.perform(
                            delete("/api/v2/admin/parking-managements/{id}/parking-map-images", VALID_ID)
                                    .header("Authorization", ADMIN_TOKEN)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(body)))
                    .andExpect(status().isBadRequest())
                    .andDo(ParkingManagementDoc.errorSnippet("주차맵이미지 삭제시 id가 NULL"));
            assertError(result, "C013", "must not be null", 400);
        }
    }

    @Nested
    class 인증_실패 {

        @Test
        void 생성_권한없음() throws Exception {
            ResultActions result = mockMvc.perform(
                            post("/api/v2/admin/festivals/{festivalId}/parking-managements", VALID_ID)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(validCreateBody)))
                    .andExpect(status().isUnauthorized())
                    .andDo(ParkingManagementDoc.errorSnippet("주차관리 생성시 인증 거부"));
            assertError(result, "A002", "Unauthenticated", 401);
        }

        @Test
        void 단건조회_권한없음() throws Exception {
            ResultActions result = mockMvc.perform(get("/api/v2/admin/parking-managements/{id}", VALID_ID))
                    .andExpect(status().isUnauthorized())
                    .andDo(ParkingManagementDoc.errorSnippet("어드민 단건조회 권한없음"));
            assertError(result, "A002", "Unauthenticated", 401);
        }

        @Test
        void 수정_권한없음() throws Exception {
            ResultActions result = mockMvc.perform(put("/api/v2/admin/parking-managements/{id}", VALID_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validCreateBody)))
                    .andExpect(status().isUnauthorized())
                    .andDo(ParkingManagementDoc.errorSnippet("주차관리 수정시 인증 거부"));
            assertError(result, "A002", "Unauthenticated", 401);
        }

        @Test
        void 서브페이지_헤더명_수정_권한없음() throws Exception {
            Map<String, Object> body = Map.of("subPageHeaderName", "주차구역 안내");
            ResultActions result = mockMvc.perform(
                            patch("/api/v2/admin/parking-managements/{id}/sub-page-header-name", VALID_ID)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(body)))
                    .andExpect(status().isUnauthorized())
                    .andDo(ParkingManagementDoc.errorSnippet("서브페이지 수정시 인증 거부"));
            assertError(result, "A002", "Unauthenticated", 401);
        }

        @Test
        void 정렬변경_권한없음() throws Exception {
            Map<String, Object> body = Map.of("imageIds", List.of(1L, 2L));
            ResultActions result = mockMvc.perform(
                            patch("/api/v2/admin/parking-managements/{id}/parking-map-images/display-order", VALID_ID)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(body)))
                    .andExpect(status().isUnauthorized())
                    .andDo(ParkingManagementDoc.errorSnippet("주차맵이미지 순서변경 인증 거부"));
            assertError(result, "A002", "Unauthenticated", 401);
        }

        @Test
        void 이미지삭제_권한없음() throws Exception {
            Map<String, Object> body = Map.of("imageIds", List.of(1L, 2L));
            ResultActions result = mockMvc.perform(
                            delete("/api/v2/admin/parking-managements/{id}/parking-map-images", VALID_ID)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(body)))
                    .andExpect(status().isUnauthorized())
                    .andDo(ParkingManagementDoc.errorSnippet("주차맵이미지 삭제시 인증 거부"));
            assertError(result, "A002", "Unauthenticated", 401);
        }
    }

    @Nested
    class 조회_엔드포인트 {

        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        void 단건조회_ADNMIN_성공() throws Exception {
            ParkingManagementResDto res = new ParkingManagementResDto();
            ReflectionTestUtils.setField(res, "id", 1L);
            ReflectionTestUtils.setField(res, "headerName", "주차 안내");
            ReflectionTestUtils.setField(res, "parkingInfoType", "BASIC");
            ReflectionTestUtils.setField(res, "title", "캠퍼스 주차 정보");
            ReflectionTestUtils.setField(res, "description", "요약");
            ReflectionTestUtils.setField(res, "buttonName", "자세히");
            ReflectionTestUtils.setField(res, "buttonTargetUrl", "https://example.com");
            ReflectionTestUtils.setField(res, "backgroundImage", "https://cdn/bg.png");
            ReflectionTestUtils.setField(res, "visible", false);

            given(parkingManagementService.getParkingManagement(VALID_ID)).willReturn(res);

            mockMvc.perform(get("/api/v2/admin/parking-managements/{id}", VALID_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.headerName").value("주차 안내"))
                    .andExpect(jsonPath("$.parkingInfoType").value("BASIC"))
                    .andExpect(jsonPath("$.title").value("캠퍼스 주차 정보"))
                    .andExpect(jsonPath("$.visible").value(false))
                    .andExpect(jsonPath("$.description").value(res.getDescription()))
                    .andExpect(jsonPath("$.buttonName").value(res.getButtonName()))
                    .andExpect(jsonPath("$.buttonTargetUrl").value(res.getButtonTargetUrl()))
                    .andExpect(jsonPath("$.backgroundImage").value(res.getBackgroundImage()))
                    .andDo(ParkingManagementDoc.getOne());
        }

        @Test
        void 단건조회_USER_성공() throws Exception {
            ParkingManagementResDto res = new ParkingManagementResDto();
            ReflectionTestUtils.setField(res, "id", 1L);
            ReflectionTestUtils.setField(res, "headerName", "주차 안내");
            ReflectionTestUtils.setField(res, "parkingInfoType", "BASIC");
            ReflectionTestUtils.setField(res, "title", "캠퍼스 주차 정보");
            ReflectionTestUtils.setField(res, "description", "요약");
            ReflectionTestUtils.setField(res, "buttonName", "자세히");
            ReflectionTestUtils.setField(res, "buttonTargetUrl", "https://example.com");
            ReflectionTestUtils.setField(res, "backgroundImage", "https://cdn/bg.png");
            ReflectionTestUtils.setField(res, "visible", true);

            given(parkingManagementService.getVisibleParkingManagement(VALID_ID))
                    .willReturn(res);

            mockMvc.perform(get("/api/v2/user/parking-managements/{id}", VALID_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1L))
                    .andExpect(jsonPath("$.headerName").value("주차 안내"))
                    .andExpect(jsonPath("$.parkingInfoType").value("BASIC"))
                    .andExpect(jsonPath("$.title").value("캠퍼스 주차 정보"))
                    .andExpect(jsonPath("$.visible").value(true))
                    .andExpect(jsonPath("$.description").value(res.getDescription()))
                    .andExpect(jsonPath("$.buttonName").value(res.getButtonName()))
                    .andExpect(jsonPath("$.buttonTargetUrl").value(res.getButtonTargetUrl()))
                    .andExpect(jsonPath("$.backgroundImage").value(res.getBackgroundImage()))
                    .andDo(ParkingManagementDoc.getOneForUser());
        }

        @Test
        void 서브페이지조회_성공() throws Exception {
            ParkingManagementSubPageResDto res = new ParkingManagementSubPageResDto();
            ImageDto imageDto1 = new ImageDto();
            ImageDto imageDto2 = new ImageDto();
            ReflectionTestUtils.setField(imageDto1, "id", 1L);
            ReflectionTestUtils.setField(imageDto1, "image", "image1");
            ReflectionTestUtils.setField(imageDto2, "id", 2L);
            ReflectionTestUtils.setField(imageDto2, "image", "image2");
            ReflectionTestUtils.setField(res, "subPageHeaderName", "주차구역 안내");
            ReflectionTestUtils.setField(res, "images", List.of(imageDto1, imageDto2));

            given(parkingManagementService.getParkingManagementSubPage(VALID_ID))
                    .willReturn(res);

            mockMvc.perform(get("/api/v2/admin/parking-managements/{id}/sub-page-info", VALID_ID)
                            .header("Authorization", ADMIN_TOKEN))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.subPageHeaderName").value("주차구역 안내"))
                    .andExpect(jsonPath("$.images.length()").value(2))
                    .andDo(ParkingManagementDoc.getSubPageInfo());
        }
    }
}
