package com.halo.eventer.domain.widget.controller;

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
import com.halo.eventer.domain.widget.dto.main_widget.MainWidgetCreateDto;
import com.halo.eventer.domain.widget.dto.main_widget.MainWidgetResDto;
import com.halo.eventer.domain.widget.service.MainWidgetService;
import com.halo.eventer.global.config.ControllerTestSecurityBeans;
import com.halo.eventer.global.config.security.SecurityConfig;
import com.halo.eventer.global.security.provider.JwtProvider;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.util.ReflectionTestUtils.setField;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MainWidgetController.class)
@AutoConfigureMockMvc
@SuppressWarnings("NonAsciiCharacters")
@ActiveProfiles("test")
@Import({ControllerTestSecurityBeans.class, SecurityConfig.class})
public class MainWidgetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MainWidgetService mainWidgetService;

    @MockitoBean
    private JwtProvider jwtProvider;

    private MainWidgetCreateDto mainWidgetCreateDto;
    private MainWidgetResDto mainWidgetResDto;

    @BeforeEach
    void setUp() {
        mainWidgetCreateDto = WidgetFixture.메인_위젯_생성_DTO();
        mainWidgetResDto = new MainWidgetResDto();
        setField(mainWidgetResDto, "id", 1L);
        setField(mainWidgetResDto, "name", mainWidgetCreateDto.getName());
        setField(mainWidgetResDto, "url", mainWidgetCreateDto.getUrl());
        setField(mainWidgetResDto, "image", mainWidgetCreateDto.getImage());
        setField(mainWidgetResDto, "description", mainWidgetCreateDto.getDescription());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 메인위젯_생성_테스트() throws Exception {
        // given
        final long festivalId = 1L;
        given(mainWidgetService.create(eq(1L), any(MainWidgetCreateDto.class))).willReturn(mainWidgetResDto);

        // when & then
        mockMvc.perform(post("/api/mainWidgets")
                        .param("festivalId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mainWidgetCreateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(mainWidgetCreateDto.getName()))
                .andExpect(jsonPath("$.url").value(mainWidgetCreateDto.getUrl()))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.image").value(mainWidgetCreateDto.getImage()))
                .andExpect(jsonPath("$.description").value(mainWidgetCreateDto.getDescription()));
        verify(mainWidgetService, times(1)).create(eq(festivalId), any(MainWidgetCreateDto.class));
    }

    @Test
    void 메인위젯_생성시_권한_예외() throws Exception {
        mockMvc.perform(post("/api/mainWidgets")
                        .param("festivalId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mainWidgetCreateDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 메인위젯_리스트_조회() throws Exception {
        // given
        final long festivalId = 1L;
        given(mainWidgetService.getAllMainWidget(festivalId)).willReturn(List.of(mainWidgetResDto));

        // when & then
        mockMvc.perform(get("/api/mainWidgets").param("festivalId", "1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value(mainWidgetCreateDto.getName()))
                .andExpect(jsonPath("$[0].url").value(mainWidgetCreateDto.getUrl()))
                .andExpect(jsonPath("$[0].image").value(mainWidgetCreateDto.getImage()))
                .andExpect(jsonPath("$[0].description").value(mainWidgetCreateDto.getDescription()));
        verify(mainWidgetService, times(1)).getAllMainWidget(eq(festivalId));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 메인위젯_수정() throws Exception {
        // given
        final long mainWidgetId = 1L;
        given(mainWidgetService.update(eq(mainWidgetId), any(MainWidgetCreateDto.class)))
                .willReturn(mainWidgetResDto);

        // when & then
        mockMvc.perform(patch("/api/mainWidgets")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mainWidgetResDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(mainWidgetCreateDto.getName()))
                .andExpect(jsonPath("$.url").value(mainWidgetCreateDto.getUrl()))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.image").value(mainWidgetCreateDto.getImage()))
                .andExpect(jsonPath("$.description").value(mainWidgetCreateDto.getDescription()));
        verify(mainWidgetService, times(1)).update(eq(mainWidgetId), any(MainWidgetCreateDto.class));
    }

    @Test
    void 메인위젯_수정_권한_예외() throws Exception {
        // given
        final long mainWidgetId = 1L;
        given(mainWidgetService.update(eq(mainWidgetId), any(MainWidgetCreateDto.class)))
                .willReturn(mainWidgetResDto);

        // when & then
        mockMvc.perform(patch("/api/mainWidgets")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mainWidgetResDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 메인위젯_삭제() throws Exception {
        // given
        final long mainWidgetId = 1L;
        doNothing().when(mainWidgetService).delete(eq(mainWidgetId));

        // when & then
        mockMvc.perform(delete("/api/mainWidgets")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mainWidgetResDto)))
                .andExpect(status().isOk());
        verify(mainWidgetService, times(1)).delete(eq(mainWidgetId));
    }

    @Test
    void 메인위젯_삭제_권한_예외() throws Exception {
        // given
        final long mainWidgetId = 1L;
        doNothing().when(mainWidgetService).delete(eq(mainWidgetId));

        // when & then
        mockMvc.perform(delete("/api/mainWidgets")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mainWidgetResDto)))
                .andExpect(status().isUnauthorized());
    }
}
