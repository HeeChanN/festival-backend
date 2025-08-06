package com.halo.eventer.domain.widget.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
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
import com.halo.eventer.domain.widget.WidgetFixture;
import com.halo.eventer.domain.widget.dto.up_widget.UpWidgetCreateDto;
import com.halo.eventer.domain.widget.dto.up_widget.UpWidgetResDto;
import com.halo.eventer.domain.widget.service.UpWidgetService;
import com.halo.eventer.global.common.page.PagedResponse;
import com.halo.eventer.global.common.sort.SortOption;
import com.halo.eventer.global.config.ControllerTestSecurityBeans;
import com.halo.eventer.global.config.security.SecurityConfig;
import com.halo.eventer.global.security.provider.JwtProvider;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.util.ReflectionTestUtils.setField;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UpWidgetController.class)
@AutoConfigureMockMvc
@SuppressWarnings("NonAsciiCharacters")
@ActiveProfiles("test")
@Import({ControllerTestSecurityBeans.class, SecurityConfig.class})
public class UpWidgetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UpWidgetService upWidgetService;

    @MockitoBean
    private JwtProvider jwtProvider;

    private UpWidgetCreateDto upWidgetCreateDto;
    private UpWidgetResDto upWidgetResDto;

    @BeforeEach
    void setUp() {
        upWidgetCreateDto = WidgetFixture.상단_위젯_생성_DTO();
        upWidgetResDto = new UpWidgetResDto();
        setField(upWidgetResDto, "id", 1L);
        setField(upWidgetResDto, "name", upWidgetCreateDto.getName());
        setField(upWidgetResDto, "url", upWidgetCreateDto.getUrl());
        setField(upWidgetResDto, "periodStart", LocalDateTime.now());
        setField(upWidgetResDto, "periodEnd", LocalDateTime.now());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 상단위젯_생성_테스트() throws Exception {
        // given
        final long festivalId = 1L;
        given(upWidgetService.create(eq(1L), any(UpWidgetCreateDto.class))).willReturn(upWidgetResDto);

        // when & then
        mockMvc.perform(post("/api/upWidgets")
                        .param("festivalId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(upWidgetCreateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(upWidgetCreateDto.getName()))
                .andExpect(jsonPath("$.url").value(upWidgetCreateDto.getUrl()))
                .andExpect(jsonPath("$.id").value(1L));
        verify(upWidgetService, times(1)).create(eq(festivalId), any(UpWidgetCreateDto.class));
    }

    @Test
    void 상단위젯_생성시_권한_예외() throws Exception {
        mockMvc.perform(post("/api/upWidgets")
                        .param("festivalId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(upWidgetCreateDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 상단위젯_단일_조회() throws Exception {
        // given
        final long upWidgetId = 1L;
        given(upWidgetService.getUpWidget(eq(upWidgetId))).willReturn(upWidgetResDto);

        // when
        mockMvc.perform(get("/api/upWidgets/{upWidgetId}", 1L).param("upWidgetId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(upWidgetResDto.getName()))
                .andExpect(jsonPath("$.url").value(upWidgetResDto.getUrl()))
                .andExpect(jsonPath("$.id").value(1L));
        verify(upWidgetService, times(1)).getUpWidget(eq(upWidgetId));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 상단위젯_리스트_조회() throws Exception {
        // given
        final long festivalId = 1L;
        PagedResponse<UpWidgetResDto> pagedResponse = new PagedResponse<>();
        given(upWidgetService.getUpWidgetsWithOffsetPaging(eq(festivalId), any(SortOption.class), anyInt(), anyInt()))
                .willReturn(pagedResponse);

        // when & then
        mockMvc.perform(get("/api/upWidgets")
                        .param("festivalId", "1")
                        .param("sortOption", SortOption.CREATED_AT.name())
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(10))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(pagedResponse)));
        verify(upWidgetService, times(1))
                .getUpWidgetsWithOffsetPaging(eq(festivalId), any(SortOption.class), anyInt(), anyInt());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 상단위젯_수정() throws Exception {
        // given
        final long upWidgetId = 1L;
        given(upWidgetService.update(eq(upWidgetId), any(UpWidgetCreateDto.class)))
                .willReturn(upWidgetResDto);

        // when & then
        mockMvc.perform(patch("/api/upWidgets")
                        .param("upWidgetId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(upWidgetCreateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(upWidgetCreateDto.getName()))
                .andExpect(jsonPath("$.url").value(upWidgetCreateDto.getUrl()))
                .andExpect(jsonPath("$.id").value(1L));
        verify(upWidgetService, times(1)).update(eq(upWidgetId), any(UpWidgetCreateDto.class));
    }

    @Test
    void 상단위젯_수정_권한_예외() throws Exception {
        // given
        final long upWidgetId = 1L;
        given(upWidgetService.update(eq(upWidgetId), any(UpWidgetCreateDto.class)))
                .willReturn(upWidgetResDto);

        // when & then
        mockMvc.perform(patch("/api/upWidgets")
                        .param("upWidgetId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(upWidgetCreateDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 상단위젯_삭제() throws Exception {
        // given
        final long upWidgetId = 1L;
        doNothing().when(upWidgetService).delete(eq(upWidgetId));

        // when & then
        mockMvc.perform(delete("/api/upWidgets").param("upWidgetId", "1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(upWidgetService, times(1)).delete(eq(upWidgetId));
    }

    @Test
    void 상단위젯_삭제_권한_예외() throws Exception {
        // given
        final long upWidgetId = 1L;
        doNothing().when(upWidgetService).delete(eq(upWidgetId));

        // when & then
        mockMvc.perform(delete("/api/upWidgets").param("upWidgetId", "1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void 상단위젯_오늘날짜로_조회() throws Exception {
        final long festivalId = 1L;
        LocalDateTime now = LocalDateTime.of(2025, 5, 10, 12, 0, 0);
        String nowParam = now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        given(upWidgetService.getUpWidgetsByNow(eq(festivalId), eq(now))).willReturn(List.of(upWidgetResDto));

        mockMvc.perform(get("/api/upWidgets/datetime")
                        .param("festivalId", String.valueOf(festivalId))
                        .param("now", nowParam)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        then(upWidgetService).should(times(1)).getUpWidgetsByNow(festivalId, now);
    }
}
