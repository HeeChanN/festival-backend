package com.halo.eventer.domain.widget.controller;

import java.util.List;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.halo.eventer.domain.widget.WidgetFixture;
import com.halo.eventer.domain.widget.dto.down_widget.DownWidgetCreateDto;
import com.halo.eventer.domain.widget.dto.down_widget.DownWidgetResDto;
import com.halo.eventer.domain.widget.service.DownWidgetService;
import com.halo.eventer.global.common.dto.OrderUpdateRequest;
import com.halo.eventer.global.config.ControllerTestSecurityBeans;
import com.halo.eventer.global.config.security.SecurityConfig;
import com.halo.eventer.global.security.provider.JwtProvider;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DownWidgetController.class)
@AutoConfigureMockMvc
@SuppressWarnings("NonAsciiCharacters")
@ActiveProfiles("test")
@Import({ControllerTestSecurityBeans.class, SecurityConfig.class})
public class DownWidgetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DownWidgetService downWidgetService;

    @MockBean
    private JwtProvider jwtProvider;

    private DownWidgetCreateDto downWidgetCreateDto;
    private DownWidgetResDto downWidgetResDto;

    @BeforeEach
    void setUp() {
        downWidgetCreateDto = WidgetFixture.하단_위젯_생성_DTO();
        downWidgetResDto = new DownWidgetResDto();
        setField(downWidgetResDto, "id", 1L);
        setField(downWidgetResDto, "name", downWidgetCreateDto.getName());
        setField(downWidgetResDto, "url", downWidgetCreateDto.getUrl());
        setField(downWidgetResDto, "displayOrder", 11);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 하단위젯_생성_테스트() throws Exception {
        // given
        final long festivalId = 1L;
        given(downWidgetService.create(eq(1L), any(DownWidgetCreateDto.class))).willReturn(downWidgetResDto);

        // when & then
        mockMvc.perform(post("/api/downWidgets")
                        .param("festivalId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(downWidgetCreateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(downWidgetCreateDto.getName()))
                .andExpect(jsonPath("$.url").value(downWidgetCreateDto.getUrl()))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.displayOrder").value(11));
        verify(downWidgetService, times(1)).create(eq(festivalId), any(DownWidgetCreateDto.class));
    }

    @Test
    void 하단위젯_생성시_권한_예외() throws Exception {
        mockMvc.perform(post("/api/downWidgets")
                        .param("festivalId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(downWidgetCreateDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 하단위젯_리스트_조회() throws Exception {
        // given
        final long festivalId = 1L;
        given(downWidgetService.getAllDownWidgets(festivalId)).willReturn(List.of(downWidgetResDto));

        // when & then
        mockMvc.perform(get("/api/downWidgets").param("festivalId", "1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value(downWidgetCreateDto.getName()))
                .andExpect(jsonPath("$[0].url").value(downWidgetCreateDto.getUrl()))
                .andExpect(jsonPath("$[0].displayOrder").value(11));
        verify(downWidgetService, times(1)).getAllDownWidgets(eq(festivalId));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 하단위젯_수정() throws Exception {
        // given
        final long downWidgetId = 1L;
        given(downWidgetService.update(eq(downWidgetId), any(DownWidgetCreateDto.class)))
                .willReturn(downWidgetResDto);

        // when & then
        mockMvc.perform(patch("/api/downWidgets")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(downWidgetCreateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(downWidgetCreateDto.getName()))
                .andExpect(jsonPath("$.url").value(downWidgetCreateDto.getUrl()))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.displayOrder").value(11));
        verify(downWidgetService, times(1)).update(eq(downWidgetId), any(DownWidgetCreateDto.class));
    }

    @Test
    void 하단위젯_수정_권한_예외() throws Exception {
        // given
        final long downWidgetId = 1L;
        given(downWidgetService.update(eq(downWidgetId), any(DownWidgetCreateDto.class)))
                .willReturn(downWidgetResDto);

        // when & then
        mockMvc.perform(patch("/api/downWidgets")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(downWidgetCreateDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 하단위젯_삭제() throws Exception {
        // given
        final long downWidgetId = 1L;
        doNothing().when(downWidgetService).delete(eq(downWidgetId));

        // when & then
        mockMvc.perform(delete("/api/downWidgets")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(downWidgetCreateDto)))
                .andExpect(status().isOk());
        verify(downWidgetService, times(1)).delete(eq(downWidgetId));
    }

    @Test
    void 하단위젯_삭제_권한_예외() throws Exception {
        // given
        final long downWidgetId = 1L;
        doNothing().when(downWidgetService).delete(eq(downWidgetId));

        // when & then
        mockMvc.perform(delete("/api/downWidgets")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(downWidgetCreateDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 하단위젯_순서_변경() throws Exception {
        // given
        final long festivalId = 1L;
        List<OrderUpdateRequest> updateRequests = List.of();
        List<DownWidgetResDto> downWidgetResDtos = List.of(downWidgetResDto);
        given(downWidgetService.updateDisplayOrder(eq(festivalId), anyList())).willReturn(downWidgetResDtos);

        // when & then
        mockMvc.perform(patch("/api/downWidgets/displayOrder")
                        .param("festivalId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequests)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value(downWidgetCreateDto.getName()))
                .andExpect(jsonPath("$[0].url").value(downWidgetCreateDto.getUrl()))
                .andExpect(jsonPath("$[0].displayOrder").value(11));
        verify(downWidgetService, times(1)).updateDisplayOrder(eq(festivalId), anyList());
    }

    @Test
    void 하단위젯_순서_변경_권한_예외() throws Exception {
        // given
        final long festivalId = 1L;
        List<OrderUpdateRequest> updateRequests = List.of();

        // when & then
        mockMvc.perform(patch("/api/downWidgets/displayOrder")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(downWidgetCreateDto)))
                .andExpect(status().isUnauthorized());
    }
}
