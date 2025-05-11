package com.halo.eventer.domain.map.controller;

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
import com.halo.eventer.domain.map.dto.mapcategory.MapCategoryImageDto;
import com.halo.eventer.domain.map.dto.mapcategory.MapCategoryResDto;
import com.halo.eventer.domain.map.service.MapCategoryService;
import com.halo.eventer.global.common.dto.OrderUpdateRequest;
import com.halo.eventer.global.config.ControllerTestSecurityBeans;
import com.halo.eventer.global.config.security.SecurityConfig;
import com.halo.eventer.global.security.provider.JwtProvider;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MapCategoryController.class)
@AutoConfigureMockMvc
@SuppressWarnings("NonAsciiCharacters")
@ActiveProfiles("test")
@Import({ControllerTestSecurityBeans.class, SecurityConfig.class})
public class MapCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MapCategoryService mapCategoryService;

    @MockBean
    private JwtProvider jwtProvider;

    private final Long festivalId = 1L;
    private final Long mapCategoryId = 1L;

    private MapCategoryImageDto mapCategoryImageDto;

    @BeforeEach
    void setUp() {
        mapCategoryImageDto = MapCategoryImageDto.of("핀", "아이콘");
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 어드민_지도카테고리_생성() throws Exception {
        // given
        given(mapCategoryService.create(festivalId, "카테고리")).willReturn(List.of(new MapCategoryResDto()));

        // when & then
        mockMvc.perform(post("/mapCategory/{festivalId}", festivalId)
                        .param("categoryName", "카테고리")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void 일반유저_지도카테고리_생성_인증거부() throws Exception {
        // given
        given(mapCategoryService.create(festivalId, "카테고리")).willReturn(List.of(new MapCategoryResDto()));

        // when & then
        mockMvc.perform(post("/mapCategory/{festivalId}", festivalId)
                        .param("categoryName", "카테고리")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void 지도카테고리와_관련된_이미지_조회() throws Exception {
        // given
        given(mapCategoryService.getIconAndPin(any())).willReturn(mapCategoryImageDto);

        // when & then
        mockMvc.perform(get("/mapCategory/image").param("mapCategoryId", "1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pin").value("핀"))
                .andExpect(jsonPath("$.icon").value("아이콘"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 어드민_지도카테고리_아이콘_핀_수정() throws Exception {
        // given
        doNothing().when(mapCategoryService).updateIconAndPin(any(), any());

        // when & then
        mockMvc.perform(patch("/mapCategory/image/{mapCategoryId}", mapCategoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mapCategoryImageDto)))
                .andExpect(status().isOk());
        verify(mapCategoryService, times(1)).updateIconAndPin(any(), any());
    }

    @Test
    void 일반유저_지도카테고리_아이콘_핀_수정_인증거부() throws Exception {
        // given
        doNothing().when(mapCategoryService).updateIconAndPin(any(), any());

        // when & then
        mockMvc.perform(patch("/mapCategory/image/{mapCategoryId}", mapCategoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mapCategoryImageDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void 지도카테고리_리스트_조회() throws Exception {
        // given
        given(mapCategoryService.getMapCategories(any())).willReturn(List.of(new MapCategoryResDto()));

        // when & then
        mockMvc.perform(get("/mapCategory").param("festivalId", "1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 어드민_지도카테고리_삭제() throws Exception {
        // given
        given(mapCategoryService.delete(any(), any())).willReturn(List.of(new MapCategoryResDto()));

        // when & then
        mockMvc.perform(delete("/mapCategory/{mapCategoryId}", mapCategoryId)
                        .param("festivalId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void 일반유저_지도카테고리_삭제_인증거부() throws Exception {
        // given
        given(mapCategoryService.delete(any(), any())).willReturn(List.of(new MapCategoryResDto()));

        // when & then
        mockMvc.perform(delete("/mapCategory/{mapCategoryId}", mapCategoryId)
                        .param("festivalId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 어드민_지도카테고리_순서_수정() throws Exception {
        given(mapCategoryService.updateDisplayOrder(any(), any())).willReturn(List.of(new MapCategoryResDto()));
        List<OrderUpdateRequest> orderUpdateRequests = List.of(new OrderUpdateRequest());

        // when & then
        mockMvc.perform(patch("/mapCategory/displayOrder")
                        .param("festivalId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderUpdateRequests)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void 일반유저_지도카테고리_순서_수정_인증거부() throws Exception {
        // given
        given(mapCategoryService.updateDisplayOrder(any(), any())).willReturn(List.of(new MapCategoryResDto()));
        List<OrderUpdateRequest> orderUpdateRequests = List.of(new OrderUpdateRequest());

        // when & then
        mockMvc.perform(patch("/mapCategory/displayOrder")
                        .param("festivalId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderUpdateRequests)))
                .andExpect(status().isUnauthorized());
    }
}
