package com.halo.eventer.domain.map.controller;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.halo.eventer.domain.map.api_docs.MapCategoryDoc;
import com.halo.eventer.domain.map.dto.mapcategory.MapCategoryCreateDto;
import com.halo.eventer.domain.map.dto.mapcategory.MapCategoryImageDto;
import com.halo.eventer.domain.map.dto.mapcategory.MapCategoryResDto;
import com.halo.eventer.domain.map.service.MapCategoryService;
import com.halo.eventer.global.common.dto.OrderUpdateRequest;
import com.halo.eventer.global.config.ControllerTestSecurityBeans;
import com.halo.eventer.global.config.security.SecurityConfig;
import com.halo.eventer.global.security.provider.JwtProvider;

import static com.halo.eventer.global.common.ApiErrorAssert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MapCategoryController.class)
@AutoConfigureRestDocs
@SuppressWarnings("NonAsciiCharacters")
@ActiveProfiles("test")
@Import({ControllerTestSecurityBeans.class, SecurityConfig.class})
public class MapCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MapCategoryService mapCategoryService;

    @MockitoBean
    private JwtProvider jwtProvider;

    private final Long festivalId = 1L;
    private final Long mapCategoryId = 1L;

    private MapCategoryImageDto mapCategoryImageDto;
    private MapCategoryCreateDto mapCategoryCreateDto;

    @BeforeEach
    void setUp() {
        mapCategoryImageDto = MapCategoryImageDto.of("핀", "아이콘");
        mapCategoryCreateDto = new MapCategoryCreateDto("화장실");
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 지도카테고리_생성() throws Exception {
        // when & then
        mockMvc.perform(post("/{festivalId}/map-categories", festivalId)
                        .content(objectMapper.writeValueAsString(mapCategoryCreateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MapCategoryDoc.createMapCategory());
    }

    @Test
    void 지도카테고리_생성_인증거부() throws Exception {
        // when & then
        ResultActions result = mockMvc.perform(post("/{festivalId}/map-categories", festivalId)
                        .content(objectMapper.writeValueAsString(mapCategoryCreateDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andDo(MapCategoryDoc.errorSnippet("지도_카테고리_생성_인증_거부"));
        assertError(result, "A002", "Unauthenticated", 401);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 지도카테고리_생성시_이름필드_길이가_길경우() throws Exception {
        // given
        MapCategoryCreateDto errorRequest = new MapCategoryCreateDto("abcdefghijklmnop");

        // when & then
        ResultActions result = mockMvc.perform(post("/{festivalId}/map-categories", festivalId)
                        .content(objectMapper.writeValueAsString(errorRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(MapCategoryDoc.errorSnippet("지도_카테고리_이름_길이_초과"));
        assertError(result, "C013", "size must be between 2 and 10", 400);
    }

    @Test
    void 지도카테고리와_관련된_이미지_조회() throws Exception {
        // given
        given(mapCategoryService.getIconAndPin(any())).willReturn(mapCategoryImageDto);

        // when & then
        mockMvc.perform(get("/map-categories/{mapCategoryId}/image", mapCategoryId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pin").value(mapCategoryImageDto.getPin()))
                .andExpect(jsonPath("$.icon").value(mapCategoryImageDto.getIcon()))
                .andDo(MapCategoryDoc.getMapCategoryImage());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 지도카테고리_아이콘_핀_수정() throws Exception {
        // given
        doNothing().when(mapCategoryService).updateIconAndPin(anyLong(), any(MapCategoryImageDto.class));

        // when & then
        mockMvc.perform(patch("/map-categories/{mapCategoryId}/image", mapCategoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mapCategoryImageDto)))
                .andExpect(status().isOk())
                .andDo(MapCategoryDoc.updateMapCategoryImage());
        verify(mapCategoryService, times(1)).updateIconAndPin(anyLong(), any(MapCategoryImageDto.class));
    }

    @Test
    void 지도카테고리_아이콘_핀_수정_인증거부() throws Exception {
        // when & then
        ResultActions result = mockMvc.perform(patch("/map-categories/{mapCategoryId}/image", mapCategoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mapCategoryImageDto)))
                .andExpect(status().isUnauthorized())
                .andDo(MapCategoryDoc.errorSnippet("지도_카테고리_이미지_수정_인증_거부"));
        assertError(result, "A002", "Unauthenticated", 401);
    }

    @Test
    void 지도카테고리_리스트_조회() throws Exception {
        // given
        MapCategoryResDto element1 = MapCategoryResDto.of(1L, "화장실", "pin", "icon", 1);
        MapCategoryResDto element2 = MapCategoryResDto.of(2L, "편의점", "pin", "icon", 1);
        given(mapCategoryService.getMapCategories(anyLong())).willReturn(List.of(element1, element2));

        // when & then
        mockMvc.perform(get("/{festivalId}/map-categories", 1L).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].mapCategoryId")
                        .value(element1.getMapCategoryId().intValue()))
                .andExpect(jsonPath("$[0].categoryName").value(element1.getCategoryName()))
                .andExpect(jsonPath("$[0].pin").value(element1.getPin()))
                .andExpect(jsonPath("$[0].icon").value(element1.getIcon()))
                .andExpect(jsonPath("$[0].displayOrder").value(element1.getDisplayOrder()))
                .andExpect(jsonPath("$[1].mapCategoryId")
                        .value(element2.getMapCategoryId().intValue()))
                .andExpect(jsonPath("$[1].categoryName").value(element2.getCategoryName()))
                .andExpect(jsonPath("$[1].pin").value(element2.getPin()))
                .andExpect(jsonPath("$[1].icon").value(element2.getIcon()))
                .andExpect(jsonPath("$[1].displayOrder").value(element2.getDisplayOrder()))
                .andDo(MapCategoryDoc.getMapCategoryList());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 지도카테고리_삭제() throws Exception {
        // given
        doNothing().when(mapCategoryService).delete(anyLong());

        // when & then
        mockMvc.perform(delete("/map-categories/{mapCategoryId}", mapCategoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MapCategoryDoc.deleteMapCategory());
        verify(mapCategoryService, times(1)).delete(anyLong());
    }

    @Test
    void 지도카테고리_삭제_인증거부() throws Exception {
        // when & then
        ResultActions result = mockMvc.perform(delete("/map-categories/{mapCategoryId}", mapCategoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andDo(MapCategoryDoc.errorSnippet("지도_카테고리_삭제_인증_거부"));
        assertError(result, "A002", "Unauthenticated", 401);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 지도카테고리_순서_수정() throws Exception {
        // given
        OrderUpdateRequest request1 = OrderUpdateRequest.of(1L, 4);
        List<OrderUpdateRequest> orderUpdateRequests = List.of(request1);

        // when & then
        mockMvc.perform(patch("/{festivalId}/map-categories/displayOrder", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer {access-token}")
                        .content(objectMapper.writeValueAsString(orderUpdateRequests)))
                .andExpect(status().isOk())
                .andDo(MapCategoryDoc.updateDisplayOrder());
        verify(mapCategoryService, times(1)).updateDisplayOrder(any(), any());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 지도카테고리_순서_수정시_순서값이_11을_초과할_경우() throws Exception {
        // given
        OrderUpdateRequest request1 = OrderUpdateRequest.of(1L, 12);
        List<OrderUpdateRequest> orderUpdateRequests = List.of(request1);

        // when & then
        ResultActions result = mockMvc.perform(patch("/{festivalId}/map-categories/displayOrder", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer {access-token}")
                        .content(objectMapper.writeValueAsString(orderUpdateRequests)))
                .andExpect(status().isBadRequest())
                .andDo(MapCategoryDoc.errorSnippet("지도_카테고리_노출순서수정_11_초과"));
        assertError(result, "C013", "must be less than or equal to 11", 400);
    }

    @Test
    void 지도카테고리_순서_수정_인증거부() throws Exception {
        // given
        OrderUpdateRequest request1 = OrderUpdateRequest.of(1L, 5);
        List<OrderUpdateRequest> orderUpdateRequests = List.of(request1);

        // when & then
        ResultActions result = mockMvc.perform(patch("/{festivalId}/map-categories/displayOrder", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer {access-token}")
                        .content(objectMapper.writeValueAsString(orderUpdateRequests)))
                .andExpect(status().isUnauthorized())
                .andDo(MapCategoryDoc.errorSnippet("지도_카테고리_노출순서수정_인증 거부"));
        assertError(result, "A002", "Unauthenticated", 401);
    }
}
