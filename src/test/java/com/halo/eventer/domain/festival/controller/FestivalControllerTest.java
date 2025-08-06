package com.halo.eventer.domain.festival.controller;

import java.util.List;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.halo.eventer.domain.festival.api_docs.FestivalDoc;
import com.halo.eventer.domain.festival.dto.*;
import com.halo.eventer.domain.festival.service.FestivalService;
import com.halo.eventer.domain.image.dto.FileDto;
import com.halo.eventer.global.config.ControllerTestSecurityBeans;
import com.halo.eventer.global.config.security.SecurityConfig;
import com.halo.eventer.global.security.provider.JwtProvider;

import static com.halo.eventer.global.common.ApiErrorAssert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FestivalController.class)
@AutoConfigureRestDocs
@SuppressWarnings("NonAsciiCharacters")
@ActiveProfiles("test")
@Import({ControllerTestSecurityBeans.class, SecurityConfig.class})
public class FestivalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private FestivalService festivalService;

    @MockitoBean
    private JwtProvider jwtProvider;

    private final String ADMIN_TOKEN = "Bearer admin-token";
    private FestivalCreateDto festivalCreateDto;
    private FestivalResDto festivalResDto;
    private ColorDto colorDto;

    @BeforeEach
    void setAll() {
        colorDto = new ColorDto("#FFFFFF", "#000000", "#CCCCCC", "#F0F0F0");
        festivalCreateDto = new FestivalCreateDto("축제", "univ");
        festivalResDto = new FestivalResDto(1L, "축제", "logo", colorDto, 127.123456, 123.123456);
    }

    @Nested
    @WithMockUser(username = "admin", roles = "ADMIN")
    class 어드민_테스트 {
        @BeforeEach
        void setUp() {
            colorDto = new ColorDto("#FFFFFF", "#000000", "#CCCCCC", "#F0F0F0");
            festivalCreateDto = new FestivalCreateDto("축제", "univ");
            festivalResDto = new FestivalResDto(1L, "축제", "logo", colorDto, 127.123456, 123.123456);
        }

        @Test
        void 축제생성() throws Exception {
            // given
            doNothing().when(festivalService).create(any(FestivalCreateDto.class));

            // when & then
            mockMvc.perform(post("/festivals")
                            .header("Authorization", ADMIN_TOKEN)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(festivalCreateDto)))
                    .andExpect(status().isOk())
                    .andDo(FestivalDoc.createFestival());
        }

        @Test
        void 축제생성시_필드값이_NULL일_경우() throws Exception {
            // given
            FestivalCreateDto errorRequest = new FestivalCreateDto(null, "univ");

            // when & then
            ResultActions resultActions = mockMvc.perform(post("/festivals")
                            .header("Authorization", ADMIN_TOKEN)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(errorRequest)))
                    .andExpect(status().isBadRequest())
                    .andDo(FestivalDoc.errorSnippet("축제_생성시_dto_내부필드_NULL"));
            assertError(resultActions, "C013", "must not be null", 400);
        }

        @Test
        void 축제수정() throws Exception {
            // given
            given(festivalService.update(any(), any())).willReturn(festivalResDto);

            // when & then
            mockMvc.perform(patch("/festivals")
                            .param("id", "1")
                            .header("Authorization", ADMIN_TOKEN)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(festivalCreateDto)))
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(festivalResDto)))
                    .andDo(FestivalDoc.updateFestival());
        }

        @Test
        void 축제삭제() throws Exception {
            // given
            doNothing().when(festivalService).delete(1L);

            // when & then
            mockMvc.perform(delete("/festivals").queryParam("id", "1").header("Authorization", ADMIN_TOKEN))
                    .andExpect(status().isOk())
                    .andDo(FestivalDoc.deleteFestival());
        }

        @Test
        void 색수정() throws Exception {
            doNothing().when(festivalService).updateColor(anyLong(), any(ColorDto.class));

            // when & then
            mockMvc.perform(post("/festivals/{festivalId}/color", 1L)
                            .header("Authorization", ADMIN_TOKEN)
                            .content(objectMapper.writeValueAsString(colorDto))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andDo(FestivalDoc.updateColor());
        }

        @Test
        void 색_수정시_입력값이_잘못된_경우() throws Exception {
            // given
            ColorDto errorRequest = new ColorDto("321", "#000000", "#CCCCCC", "#F0F0F0");

            // when & then
            ResultActions result = mockMvc.perform(post("/festivals/{festivalId}/color", 1L)
                            .header("Authorization", ADMIN_TOKEN)
                            .content(objectMapper.writeValueAsString(errorRequest))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andDo(FestivalDoc.errorSnippet("축제_색상_수정_필드_오류"));
            assertError(result, "C013", "색상은 #RGB 또는 #RRGGBB 형식이어야 합니다.", 400);
        }

        @Test
        void 로고수정() throws Exception {
            // given
            FileDto fileDto = new FileDto("logo");

            // when & then
            mockMvc.perform(post("/festivals/{festivalId}/logo", 1L)
                            .header("Authorization", ADMIN_TOKEN)
                            .content(objectMapper.writeValueAsString(fileDto))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andDo(FestivalDoc.updateLogo());
        }

        @Test
        void 위치정보_수정() throws Exception {
            // given
            FestivalLocationDto festivalLocationDto = new FestivalLocationDto();
            given(festivalService.updateLocation(any(), any())).willReturn(festivalResDto);

            // when & then
            mockMvc.perform(post("/festivals/{festivalId}/location", 1L)
                            .header("Authorization", ADMIN_TOKEN)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(festivalLocationDto)))
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(festivalResDto)))
                    .andDo(FestivalDoc.updateLocation());
        }
    }

    @Nested
    class 일반유저_테스트 {

        @Test
        void 축제생성시_토큰이_없는경우() throws Exception {
            FestivalCreateDto dto = new FestivalCreateDto("축제", "univ");
            doNothing().when(festivalService).create(any(FestivalCreateDto.class));

            ResultActions result = mockMvc.perform(post("/festivals")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isUnauthorized())
                    .andDo(FestivalDoc.errorSnippet("축제_생성_인증_거부"));
            assertError(result, "A002", "Unauthenticated", 401);
        }

        @Test
        void 축제수정시_권한이_없는_경우() throws Exception {
            // given
            given(festivalService.update(any(), any())).willReturn(festivalResDto);

            // when & then
            ResultActions result = mockMvc.perform(patch("/festivals")
                            .param("id", "1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(festivalCreateDto)))
                    .andExpect(status().isUnauthorized())
                    .andDo(FestivalDoc.errorSnippet("축제_수정_인증_거부"));
            assertError(result, "A002", "Unauthenticated", 401);
        }

        @Test
        void 축제삭제시_토큰이_없는경우() throws Exception {
            // given
            doNothing().when(festivalService).delete(1L);

            // when & then
            ResultActions result = mockMvc.perform(
                            delete("/festivals").param("id", "1").contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized())
                    .andDo(FestivalDoc.errorSnippet("축제_삭제_인증_거부"));
            assertError(result, "A002", "Unauthenticated", 401);
        }

        @Test
        void 축제색수정시_토큰이_없는경우() throws Exception {
            doNothing().when(festivalService).updateColor(1L, colorDto);

            // when & then
            ResultActions result = mockMvc.perform(post("/festivals/{festivalId}/color", 1L)
                            .content(objectMapper.writeValueAsString(festivalCreateDto))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized())
                    .andDo(FestivalDoc.errorSnippet("축제_색_수정_인증_거부"));
            assertError(result, "A002", "Unauthenticated", 401);
        }

        @Test
        void 로고수정시_토큰이_없는경우() throws Exception {
            // given
            FileDto fileDto = new FileDto("logo");

            // when & then
            ResultActions result = mockMvc.perform(post("/festivals/{festivalId}/logo", 1L)
                            .header("Authorization", ADMIN_TOKEN)
                            .content(objectMapper.writeValueAsString(fileDto))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized())
                    .andDo(FestivalDoc.errorSnippet("축제_로고_수정_인증_거부"));
            assertError(result, "A002", "Unauthenticated", 401);
        }

        @Test
        void 위치정보_수정시_토큰이_없는경우() throws Exception {
            // given
            FestivalLocationDto festivalLocationDto = new FestivalLocationDto();
            given(festivalService.updateLocation(any(), any())).willReturn(festivalResDto);

            // when & then
            ResultActions result = mockMvc.perform(post("/festivals/{festivalId}/location", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(festivalCreateDto)))
                    .andExpect(status().isUnauthorized())
                    .andDo(FestivalDoc.errorSnippet("축제_위치_정보_수정_인증_거부"));
            assertError(result, "A002", "Unauthenticated", 401);
        }
    }

    @Nested
    class 공통기능_테스트 {
        @Test
        void 축제_정보_조회() throws Exception {
            // given
            given(festivalService.findById(any())).willReturn(festivalResDto);

            // then
            mockMvc.perform(get("/festivals/{id}", 1L).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(festivalResDto)))
                    .andDo(FestivalDoc.getFestival());
        }

        @Test
        void 축제_리스트_조회() throws Exception {
            // given
            FestivalSummaryDto festivalSummaryDto = new FestivalSummaryDto(1L, "축제", "univ", 123, 123);
            given(festivalService.findAll()).willReturn(List.of(festivalSummaryDto));

            // then
            mockMvc.perform(get("/festivals").contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(1))
                    .andExpect(jsonPath("$[0].id").value(festivalSummaryDto.getId()))
                    .andExpect(jsonPath("$[0].festivalName").value(festivalSummaryDto.getFestivalName()))
                    .andExpect(jsonPath("$[0].subAddress").value(festivalSummaryDto.getSubAddress()))
                    .andExpect(jsonPath("$[0].latitude").value(festivalSummaryDto.getLatitude()))
                    .andExpect(jsonPath("$[0].longitude").value(festivalSummaryDto.getLongitude()))
                    .andDo(FestivalDoc.getFestivals());
        }
    }
}
