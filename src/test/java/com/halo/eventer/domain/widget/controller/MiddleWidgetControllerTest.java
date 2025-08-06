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
import com.halo.eventer.domain.widget.dto.middle_widget.MiddleWidgetCreateDto;
import com.halo.eventer.domain.widget.dto.middle_widget.MiddleWidgetResDto;
import com.halo.eventer.domain.widget.service.MiddleWidgetService;
import com.halo.eventer.global.common.dto.OrderUpdateRequest;
import com.halo.eventer.global.common.page.PagedResponse;
import com.halo.eventer.global.common.sort.SortOption;
import com.halo.eventer.global.config.ControllerTestSecurityBeans;
import com.halo.eventer.global.config.security.SecurityConfig;
import com.halo.eventer.global.security.provider.JwtProvider;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.util.ReflectionTestUtils.setField;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MiddleWidgetController.class)
@AutoConfigureMockMvc
@SuppressWarnings("NonAsciiCharacters")
@ActiveProfiles("test")
@Import({ControllerTestSecurityBeans.class, SecurityConfig.class})
public class MiddleWidgetControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MiddleWidgetService middleWidgetService;

    @MockitoBean
    private JwtProvider jwtProvider;

    private MiddleWidgetCreateDto middleWidgetCreateDto;
    private MiddleWidgetResDto middleWidgetResDto;

    @BeforeEach
    void setUp() {
        middleWidgetCreateDto = WidgetFixture.중간_위젯_생성_DTO();
        middleWidgetResDto = new MiddleWidgetResDto();
        setField(middleWidgetResDto, "id", 1L);
        setField(middleWidgetResDto, "name", middleWidgetCreateDto.getName());
        setField(middleWidgetResDto, "url", middleWidgetCreateDto.getUrl());
        setField(middleWidgetResDto, "image", middleWidgetCreateDto.getImage());
        setField(middleWidgetResDto, "displayOrder", 11);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 중간위젯_생성_테스트() throws Exception {
        // given
        final long festivalId = 1L;
        given(middleWidgetService.create(eq(1L), any(MiddleWidgetCreateDto.class)))
                .willReturn(middleWidgetResDto);

        // when & then
        mockMvc.perform(post("/api/middleWidgets")
                        .param("festivalId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(middleWidgetCreateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(middleWidgetCreateDto.getName()))
                .andExpect(jsonPath("$.url").value(middleWidgetCreateDto.getUrl()))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.image").value(middleWidgetCreateDto.getImage()))
                .andExpect(jsonPath("$.displayOrder").value(11));
        verify(middleWidgetService, times(1)).create(eq(festivalId), any(MiddleWidgetCreateDto.class));
    }

    @Test
    void 중간위젯_생성시_권한_예외() throws Exception {
        mockMvc.perform(post("/api/middleWidgets")
                        .param("festivalId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(middleWidgetCreateDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 중간위젯_단일조회() throws Exception {
        // given
        final long middleWidgetId = 1L;
        given(middleWidgetService.getMiddleWidget(middleWidgetId)).willReturn(middleWidgetResDto);

        // when & then
        mockMvc.perform(get("/api/middleWidgets/{id}", 1L).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value(middleWidgetCreateDto.getName()))
                .andExpect(jsonPath("$.url").value(middleWidgetCreateDto.getUrl()))
                .andExpect(jsonPath("$.image").value(middleWidgetCreateDto.getImage()))
                .andExpect(jsonPath("$.displayOrder").value(11));
        verify(middleWidgetService, times(1)).getMiddleWidget(eq(middleWidgetId));
    }

    @Test
    void 중간위젯_단일조회_권한예외() throws Exception {
        // when & then
        mockMvc.perform(get("/api/middleWidgets/{id}", 1L).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 중간위젯_리스트_조회() throws Exception {
        // given
        final long festivalId = 1L;
        PagedResponse<MiddleWidgetResDto> pagedResponse = new PagedResponse<>();
        given(middleWidgetService.getMiddleWidgetsWithOffsetPaging(
                        eq(festivalId), any(SortOption.class), anyInt(), anyInt()))
                .willReturn(pagedResponse);

        // when & then
        mockMvc.perform(get("/api/middleWidgets")
                        .param("festivalId", "1")
                        .param("sortOption", SortOption.CREATED_AT.name())
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(10))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(pagedResponse)));
        verify(middleWidgetService, times(1))
                .getMiddleWidgetsWithOffsetPaging(eq(festivalId), any(SortOption.class), anyInt(), anyInt());
    }

    @Test
    void 중간위젯_리스트_조회_권한_예외() throws Exception {
        // given
        PagedResponse<MiddleWidgetResDto> pagedResponse = new PagedResponse<>();

        // when & then
        mockMvc.perform(get("/api/middleWidgets")
                        .param("festivalId", "1")
                        .param("sortOption", SortOption.CREATED_AT.name())
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(10))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 중간위젯_수정() throws Exception {
        // given
        final long middleWidgetId = 1L;
        given(middleWidgetService.update(eq(middleWidgetId), any(MiddleWidgetCreateDto.class)))
                .willReturn(middleWidgetResDto);

        // when & then
        mockMvc.perform(patch("/api/middleWidgets")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(middleWidgetCreateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(middleWidgetCreateDto.getName()))
                .andExpect(jsonPath("$.url").value(middleWidgetCreateDto.getUrl()))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.image").value(middleWidgetCreateDto.getImage()))
                .andExpect(jsonPath("$.displayOrder").value(11));
        verify(middleWidgetService, times(1)).update(eq(middleWidgetId), any(MiddleWidgetCreateDto.class));
    }

    @Test
    void 중간위젯_수정_권한_예외() throws Exception {
        // given
        final long middleWidgetId = 1L;
        given(middleWidgetService.update(eq(middleWidgetId), any(MiddleWidgetCreateDto.class)))
                .willReturn(middleWidgetResDto);

        // when & then
        mockMvc.perform(patch("/api/middleWidgets")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(middleWidgetCreateDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 중간위젯_삭제() throws Exception {
        // given
        final long middleWidgetId = 1L;
        doNothing().when(middleWidgetService).delete(eq(middleWidgetId));

        // when & then
        mockMvc.perform(delete("/api/middleWidgets")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(middleWidgetCreateDto)))
                .andExpect(status().isOk());
        verify(middleWidgetService, times(1)).delete(eq(middleWidgetId));
    }

    @Test
    void 중간위젯_삭제_권한_예외() throws Exception {
        // given
        final long middleWidgetId = 1L;
        doNothing().when(middleWidgetService).delete(eq(middleWidgetId));

        // when & then
        mockMvc.perform(delete("/api/middleWidgets")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(middleWidgetCreateDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 중간위젯_순서_변경() throws Exception {
        // given
        final long festivalId = 1L;
        List<OrderUpdateRequest> updateRequests = List.of();
        List<MiddleWidgetResDto> middleWidgetResDtos = List.of(middleWidgetResDto);
        given(middleWidgetService.updateDisplayOrder(eq(festivalId), anyList())).willReturn(middleWidgetResDtos);

        // when & then
        mockMvc.perform(patch("/api/middleWidgets/displayOrder")
                        .param("festivalId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequests)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value(middleWidgetCreateDto.getName()))
                .andExpect(jsonPath("$[0].url").value(middleWidgetCreateDto.getUrl()))
                .andExpect(jsonPath("$[0].image").value(middleWidgetCreateDto.getImage()))
                .andExpect(jsonPath("$[0].displayOrder").value(11));
        verify(middleWidgetService, times(1)).updateDisplayOrder(eq(festivalId), anyList());
    }

    @Test
    void 중간위젯_순서_변경_권한_예외() throws Exception {
        // given
        final long festivalId = 1L;
        List<OrderUpdateRequest> updateRequests = List.of();

        // when & then
        mockMvc.perform(patch("/api/middleWidgets/displayOrder")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(middleWidgetCreateDto)))
                .andExpect(status().isUnauthorized());
    }
}
