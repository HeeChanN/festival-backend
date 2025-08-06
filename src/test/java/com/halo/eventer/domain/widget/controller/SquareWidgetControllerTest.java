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
import com.halo.eventer.domain.widget.dto.square_widget.SquareWidgetCreateDto;
import com.halo.eventer.domain.widget.dto.square_widget.SquareWidgetResDto;
import com.halo.eventer.domain.widget.service.SquareWidgetService;
import com.halo.eventer.global.common.dto.OrderUpdateRequest;
import com.halo.eventer.global.common.page.PagedResponse;
import com.halo.eventer.global.common.sort.SortOption;
import com.halo.eventer.global.config.ControllerTestSecurityBeans;
import com.halo.eventer.global.config.security.SecurityConfig;
import com.halo.eventer.global.security.provider.JwtProvider;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.util.ReflectionTestUtils.setField;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SquareWidgetController.class)
@AutoConfigureMockMvc
@SuppressWarnings("NonAsciiCharacters")
@ActiveProfiles("test")
@Import({ControllerTestSecurityBeans.class, SecurityConfig.class})
public class SquareWidgetControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SquareWidgetService squareWidgetService;

    @MockitoBean
    private JwtProvider jwtProvider;

    private SquareWidgetCreateDto squareWidgetCreateDto;
    private SquareWidgetResDto squareWidgetResDto;

    @BeforeEach
    void setUp() {
        squareWidgetCreateDto = WidgetFixture.정사각형_위젯_생성_DTO();
        squareWidgetResDto = new SquareWidgetResDto();
        setField(squareWidgetResDto, "id", 1L);
        setField(squareWidgetResDto, "name", squareWidgetCreateDto.getName());
        setField(squareWidgetResDto, "url", squareWidgetCreateDto.getUrl());
        setField(squareWidgetResDto, "description", squareWidgetCreateDto.getDescription());
        setField(squareWidgetResDto, "icon", squareWidgetCreateDto.getImage());
        setField(squareWidgetResDto, "displayOrder", 11);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 정사각형위젯_생성_테스트() throws Exception {
        // given
        final long festivalId = 1L;
        given(squareWidgetService.create(eq(1L), any(SquareWidgetCreateDto.class)))
                .willReturn(squareWidgetResDto);

        // when & then
        mockMvc.perform(post("/api/squareWidgets")
                        .param("festivalId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(squareWidgetCreateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(squareWidgetCreateDto.getName()))
                .andExpect(jsonPath("$.url").value(squareWidgetCreateDto.getUrl()))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.description").value(squareWidgetCreateDto.getDescription()))
                .andExpect(jsonPath("$.icon").value(squareWidgetCreateDto.getImage()))
                .andExpect(jsonPath("$.displayOrder").value(11));
        verify(squareWidgetService, times(1)).create(eq(festivalId), any(SquareWidgetCreateDto.class));
    }

    @Test
    void 중간위젯_생성시_권한_예외() throws Exception {
        mockMvc.perform(post("/api/squareWidgets")
                        .param("festivalId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(squareWidgetCreateDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 정사각형위젯_단일조회() throws Exception {
        // given
        final long squareWidgetId = 1L;
        given(squareWidgetService.getSquareWidget(squareWidgetId)).willReturn(squareWidgetResDto);

        // when & then
        mockMvc.perform(get("/api/squareWidgets/{id}", 1L).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value(squareWidgetCreateDto.getName()))
                .andExpect(jsonPath("$.url").value(squareWidgetCreateDto.getUrl()))
                .andExpect(jsonPath("$.description").value(squareWidgetCreateDto.getDescription()))
                .andExpect(jsonPath("$.icon").value(squareWidgetCreateDto.getImage()))
                .andExpect(jsonPath("$.displayOrder").value(11));
        verify(squareWidgetService, times(1)).getSquareWidget(eq(squareWidgetId));
    }

    @Test
    void 정사각형위젯_단일조회_권한예외() throws Exception {
        // when & then
        mockMvc.perform(get("/api/squareWidgets/{id}", 1L).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 정사각형위젯_리스트_조회() throws Exception {
        // given
        final long festivalId = 1L;
        PagedResponse<SquareWidgetResDto> pagedResponse = new PagedResponse<>();
        given(squareWidgetService.getSquareWidgetsWithOffsetPaging(
                        eq(festivalId), any(SortOption.class), anyInt(), anyInt()))
                .willReturn(pagedResponse);

        // when & then
        mockMvc.perform(get("/api/squareWidgets")
                        .param("festivalId", "1")
                        .param("sortOption", SortOption.CREATED_AT.name())
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(10))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(pagedResponse)));
        verify(squareWidgetService, times(1))
                .getSquareWidgetsWithOffsetPaging(eq(festivalId), any(SortOption.class), anyInt(), anyInt());
    }

    @Test
    void 정사각형위젯_리스트_조회_권한_예외() throws Exception {
        // given
        PagedResponse<SquareWidgetResDto> pagedResponse = new PagedResponse<>();

        // when & then
        mockMvc.perform(get("/api/squareWidgets")
                        .param("festivalId", "1")
                        .param("sortOption", SortOption.CREATED_AT.name())
                        .param("page", String.valueOf(0))
                        .param("size", String.valueOf(10))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 정사각형위젯_수정() throws Exception {
        // given
        final long squareWidgetId = 1L;
        given(squareWidgetService.update(eq(squareWidgetId), any(SquareWidgetCreateDto.class)))
                .willReturn(squareWidgetResDto);

        // when & then
        mockMvc.perform(patch("/api/squareWidgets")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(squareWidgetCreateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(squareWidgetCreateDto.getName()))
                .andExpect(jsonPath("$.url").value(squareWidgetCreateDto.getUrl()))
                .andExpect(jsonPath("$.description").value(squareWidgetCreateDto.getDescription()))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.icon").value(squareWidgetCreateDto.getImage()))
                .andExpect(jsonPath("$.displayOrder").value(11));
        verify(squareWidgetService, times(1)).update(eq(squareWidgetId), any(SquareWidgetCreateDto.class));
    }

    @Test
    void 정사각형위젯_수정_권한_예외() throws Exception {
        // given
        final long squareWidgetId = 1L;
        given(squareWidgetService.update(eq(squareWidgetId), any(SquareWidgetCreateDto.class)))
                .willReturn(squareWidgetResDto);

        // when & then
        mockMvc.perform(patch("/api/squareWidgets")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(squareWidgetCreateDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 정사각형위젯_삭제() throws Exception {
        // given
        final long squareWidgetId = 1L;
        doNothing().when(squareWidgetService).delete(eq(squareWidgetId));

        // when & then
        mockMvc.perform(delete("/api/squareWidgets")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(squareWidgetCreateDto)))
                .andExpect(status().isOk());
        verify(squareWidgetService, times(1)).delete(eq(squareWidgetId));
    }

    @Test
    void 정사각형위젯_삭제_권한_예외() throws Exception {
        // given
        final long squareWidgetId = 1L;
        doNothing().when(squareWidgetService).delete(eq(squareWidgetId));

        // when & then
        mockMvc.perform(delete("/api/squareWidgets")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(squareWidgetCreateDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 정사각형위젯_순서_변경() throws Exception {
        // given
        final long festivalId = 1L;
        List<OrderUpdateRequest> updateRequests = List.of();
        List<SquareWidgetResDto> middleWidgetResDtos = List.of(squareWidgetResDto);
        given(squareWidgetService.updateDisplayOrder(eq(festivalId), anyList())).willReturn(middleWidgetResDtos);

        // when & then
        mockMvc.perform(patch("/api/squareWidgets/displayOrder")
                        .param("festivalId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequests)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value(squareWidgetCreateDto.getName()))
                .andExpect(jsonPath("$[0].url").value(squareWidgetCreateDto.getUrl()))
                .andExpect(jsonPath("$[0].description").value(squareWidgetCreateDto.getDescription()))
                .andExpect(jsonPath("$[0].icon").value(squareWidgetCreateDto.getImage()))
                .andExpect(jsonPath("$[0].displayOrder").value(11));
        verify(squareWidgetService, times(1)).updateDisplayOrder(eq(festivalId), anyList());
    }

    @Test
    void 정사각형위젯_순서_변경_권한_예외() throws Exception {
        // given
        final long festivalId = 1L;
        List<OrderUpdateRequest> updateRequests = List.of();

        // when & then
        mockMvc.perform(patch("/api/squareWidgets/displayOrder")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(squareWidgetCreateDto)))
                .andExpect(status().isUnauthorized());
    }
}
