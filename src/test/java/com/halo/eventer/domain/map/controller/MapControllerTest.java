package com.halo.eventer.domain.map.controller;

import java.time.LocalDate;
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
import com.halo.eventer.domain.duration.dto.DurationResDto;
import com.halo.eventer.domain.map.api_docs.MapDoc;
import com.halo.eventer.domain.map.dto.map.*;
import com.halo.eventer.domain.map.enumtype.OperationTime;
import com.halo.eventer.domain.map.service.MapService;
import com.halo.eventer.global.config.ControllerTestSecurityBeans;
import com.halo.eventer.global.config.security.SecurityConfig;
import com.halo.eventer.global.security.provider.JwtProvider;

import static com.halo.eventer.global.common.ApiErrorAssert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MapController.class)
@AutoConfigureRestDocs
@SuppressWarnings("NonAsciiCharacters")
@ActiveProfiles("test")
@Import({ControllerTestSecurityBeans.class, SecurityConfig.class})
public class MapControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MapService mapService;

    @MockitoBean
    private JwtProvider jwtProvider;

    private MapResDto mapResDto;
    private MapCreateDto mapCreateDto;
    private MapItemDto mapItemDto1;
    private MapItemDto mapItemDto2;
    private MapUpdateDto mapUpdateDto;
    private List<MapItemDto> mapItems;

    private OperationInfoDto operationInfoDto;
    private LocationInfoDto locationInfoDto;
    private ButtonInfoDto buttonInfoDto;
    private DurationResDto durationResDto;
    private DurationBindingDto bindingDto;

    @BeforeEach
    void setUp() {
        operationInfoDto = OperationInfoDto.of("오픈시간", OperationTime.afternoon);
        locationInfoDto = LocationInfoDto.of("주소(장소 정보)", 2, 2);
        buttonInfoDto = ButtonInfoDto.of("버튼 이름", "연결 주소", "버튼 이미지");
        durationResDto = new DurationResDto(1L, LocalDate.now(), 1);
        mapItemDto1 = new MapItemDto(
                1L,
                "지도(부스) 이름",
                "icon",
                OperationTime.afternoon.toString(),
                locationInfoDto,
                "카테고리 이름",
                List.of(durationResDto));
        mapItemDto2 = new MapItemDto(
                2L,
                "지도(부스) 이름",
                "icon",
                OperationTime.afternoon.toString(),
                locationInfoDto,
                "카테고리 이름",
                List.of(durationResDto));
        mapItems = List.of(mapItemDto1, mapItemDto2);
        bindingDto = new DurationBindingDto(List.of(), List.of());
        mapUpdateDto = new MapUpdateDto(
                "new_name",
                "new_summary",
                "new_content",
                "new_thumbnail",
                "new_icon",
                operationInfoDto,
                locationInfoDto,
                buttonInfoDto,
                bindingDto);

        mapResDto = MapResDto.builder()
                .mapId(1L)
                .name("name")
                .summary("summary")
                .content("content")
                .thumbnail("thumbnail")
                .icon("icon")
                .operationInfo(operationInfoDto)
                .locationInfo(locationInfoDto)
                .buttonInfo(buttonInfoDto)
                .categoryName("categoryName")
                .durations(List.of(durationResDto))
                .build();
        mapCreateDto = new MapCreateDto(
                "name",
                "summary",
                "content",
                "thumbnail",
                "icon",
                operationInfoDto,
                locationInfoDto,
                buttonInfoDto,
                List.of(1L));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 지도요소생성() throws Exception {
        // given
        given(mapService.create(any(MapCreateDto.class), eq(1L))).willReturn(mapResDto);

        // when & then
        mockMvc.perform(post("/map-categories/{mapCategoryId}/maps", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mapCreateDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(mapResDto)))
                .andDo(MapDoc.createMap());
    }

    @Test
    void 지도요소_생성시_인증거부() throws Exception {
        // when & then
        ResultActions result = mockMvc.perform(post("/map-categories/{mapCategoryId}/maps", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mapCreateDto)))
                .andExpect(status().isUnauthorized())
                .andDo(MapDoc.errorSnippet("지도_생성_인증_거부"));
        assertError(result, "A002", "Unauthenticated", 401);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 지도요소_이름_길이가_20_초과일경우() throws Exception {
        // given
        MapCreateDto errorRequest = new MapCreateDto(
                "abcdefghijklmnopqrstuv",
                "summary",
                "content",
                "thumbnail",
                "icon",
                operationInfoDto,
                locationInfoDto,
                buttonInfoDto,
                List.of(1L));

        // when & then
        ResultActions result = mockMvc.perform(post("/map-categories/{mapCategoryId}/maps", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(errorRequest)))
                .andExpect(status().isBadRequest())
                .andDo(MapDoc.errorSnippet("지도_생성_이름_길이_초과"));
        assertError(result, "C013", "size must be between 1 and 20", 400);
    }

    @Test
    void 지도요소_단일조회() throws Exception {
        // given
        given(mapService.getMap(anyLong())).willReturn(mapResDto);

        // when & then
        mockMvc.perform(get("/maps/{mapId}", 1L).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(mapResDto)))
                .andDo(MapDoc.getMap());
    }

    @Test
    void 지도요소_리스트_조회() throws Exception {
        // given
        given(mapService.getMaps(anyLong())).willReturn(mapItems);

        // when & then
        mockMvc.perform(get("/map-categories/{mapCategoryId}/maps", 1L).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(mapItems.get(0).getId()))
                .andExpect(jsonPath("$[0].name").value(mapItems.get(0).getName()))
                .andExpect(jsonPath("$[0].operationTime").value(mapItems.get(0).getOperationTime()))
                .andExpect(jsonPath("$[0].locationInfo.address")
                        .value(mapItems.get(0).getLocationInfo().getAddress()))
                .andExpect(jsonPath("$[0].locationInfo.latitude")
                        .value(mapItems.get(0).getLocationInfo().getLatitude()))
                .andExpect(jsonPath("$[0].locationInfo.longitude")
                        .value(mapItems.get(0).getLocationInfo().getLongitude()))
                .andExpect(jsonPath("$[0].categoryName").value(mapItems.get(0).getCategoryName()))
                .andExpect(jsonPath("$[0].icon").value(mapItems.get(0).getIcon()))
                .andExpect(jsonPath("$[0].durationResDto[0].date")
                        .value(durationResDto.getDate().toString()))
                .andExpect(jsonPath("$[0].durationResDto[0].durationId").value(durationResDto.getDurationId()))
                .andExpect(jsonPath("$[0].durationResDto[0].dayNumber").value(durationResDto.getDayNumber()))
                .andDo(MapDoc.getMaps());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 지도요소_수정() throws Exception {
        // given
        MapResDto updated = new MapResDto(
                1L,
                mapUpdateDto.getName(),
                mapUpdateDto.getSummary(),
                mapUpdateDto.getContent(),
                mapUpdateDto.getThumbnail(),
                mapUpdateDto.getIcon(),
                locationInfoDto,
                operationInfoDto,
                buttonInfoDto,
                "카테고리 이름",
                List.of(durationResDto));
        given(mapService.update(anyLong(), any(MapUpdateDto.class), anyLong())).willReturn(updated);

        // when & then
        mockMvc.perform(put("/map-categories/{mapCategoryId}/maps/{mapId}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mapUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(updated)))
                .andDo(MapDoc.updateMap());
    }

    @Test
    void 지도요소_수정_인증거부() throws Exception {
        // when & then
        ResultActions result = mockMvc.perform(put("/map-categories/{mapCategoryId}/maps/{mapId}", 1L, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mapUpdateDto)))
                .andExpect(status().isUnauthorized())
                .andDo(MapDoc.errorSnippet("지도_수정_인증_거부"));
        assertError(result, "A002", "Unauthenticated", 401);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 지도요소_수정시_이름_길이가_20을_넘어갈_경우() throws Exception {
        // given
        MapUpdateDto errorRequest = new MapUpdateDto(
                "abcdefghijklmnopqrstuv",
                "new_summary",
                "new_content",
                "new_thumbnail",
                "new_icon",
                operationInfoDto,
                locationInfoDto,
                buttonInfoDto,
                bindingDto);

        // when & then
        ResultActions result = mockMvc.perform(post("/map-categories/{mapCategoryId}/maps", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(errorRequest)))
                .andExpect(status().isBadRequest())
                .andDo(MapDoc.errorSnippet("지도_수정_이름_길이_초과"));
        assertError(result, "C013", "size must be between 1 and 20", 400);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 맵_삭제() throws Exception {
        // given
        doNothing().when(mapService).delete(anyLong());

        // when & then
        mockMvc.perform(delete("/maps/{mapId}", 1L).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MapDoc.deleteMap());
        verify(mapService, times(1)).delete(anyLong());
    }

    @Test
    void 맵_삭제_권한예외() throws Exception {
        // when & then
        ResultActions result = mockMvc.perform(delete("/maps/{mapId}", 1L).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andDo(MapDoc.errorSnippet("지도_삭제_인증_거부"));
        assertError(result, "A002", "Unauthenticated", 401);
    }
}
