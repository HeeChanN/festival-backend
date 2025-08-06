package com.halo.eventer.domain.lost_item.controller;

import java.time.format.DateTimeFormatter;
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
import com.halo.eventer.domain.lost_item.LostItemFixture;
import com.halo.eventer.domain.lost_item.api_docs.LostItemDoc;
import com.halo.eventer.domain.lost_item.dto.LostItemReqDto;
import com.halo.eventer.domain.lost_item.dto.LostItemResDto;
import com.halo.eventer.domain.lost_item.dto.LostItemSummaryDto;
import com.halo.eventer.domain.lost_item.service.LostItemService;
import com.halo.eventer.global.config.ControllerTestSecurityBeans;
import com.halo.eventer.global.config.security.SecurityConfig;
import com.halo.eventer.global.security.provider.JwtProvider;

import static com.halo.eventer.domain.lost_item.LostItemFixture.*;
import static com.halo.eventer.global.common.ApiErrorAssert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LostItemController.class)
@AutoConfigureRestDocs
@SuppressWarnings("NonAsciiCharacters")
@ActiveProfiles("test")
@Import({ControllerTestSecurityBeans.class, SecurityConfig.class})
public class LostItemControllerTest {

    @MockitoBean
    private LostItemService lostItemService;

    @MockitoBean
    private JwtProvider jwtProvider;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private LostItemReqDto lostItemReqDto;

    @BeforeEach
    void setUp() {
        lostItemReqDto = LostItem_기본_생성_DTO();
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 분실물_생성() throws Exception {
        // when & then
        mockMvc.perform(post("/lost-items")
                        .queryParam("festivalId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer {access-token}")
                        .content(objectMapper.writeValueAsString(lostItemReqDto)))
                .andExpect(status().isOk())
                .andDo(LostItemDoc.createLostItem());
        verify(lostItemService, times(1)).create(anyLong(), any(LostItemReqDto.class));
    }

    @Test
    void 분실물_생성시_권한이_없는_경우() throws Exception {
        // when & then
        ResultActions result = mockMvc.perform(post("/lost-items")
                        .queryParam("festivalId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer {access-token}")
                        .content(objectMapper.writeValueAsString(lostItemReqDto)))
                .andExpect(status().isUnauthorized())
                .andDo(LostItemDoc.errorSnippet("분실물_생성시_인증_거부"));
        assertError(result, "A002", "Unauthenticated", 401);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 분실물_생성시_분실물_이름이_없는_경우() throws Exception {
        // given
        LostItemReqDto errorRequest = LostItemReqDto.builder().name(null).build();

        // when & then
        ResultActions result = mockMvc.perform(post("/lost-items")
                        .queryParam("festivalId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer {access-token}")
                        .content(objectMapper.writeValueAsString(errorRequest)))
                .andExpect(status().isBadRequest())
                .andDo(LostItemDoc.errorSnippet("분실물_생성시_name_필드_null"));
        assertError(result, "C013", "must not be null", 400);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 분실물_생성시_찾은날짜_형식이_잘못된_경우() throws Exception {
        // given
        String errorRequest =
                """
            {"name":"name","description":"description","thumbnail":"thumbnail","findDate":"2025/01/01"}
            """;

        // when & then
        ResultActions result = mockMvc.perform(post("/lost-items")
                        .queryParam("festivalId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer {access-token}")
                        .content(errorRequest))
                .andExpect(status().isBadRequest())
                .andDo(LostItemDoc.errorSnippet("분실물_생성시_날짜_형식_오류"));
        assertError(result, "C008", "Invalid JSON Format", 400);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 분실물_수정() throws Exception {
        // given
        LostItemReqDto updateDto = LostItemFixture.LostItem_수정용_DTO();
        LostItemResDto response = LostItemResDto.of(
                1L, updateDto.getName(), updateDto.getDescription(), updateDto.getThumbnail(), updateDto.getFindDate());
        given(lostItemService.update(anyLong(), any(LostItemReqDto.class))).willReturn(response);

        // when & then
        mockMvc.perform(put("/lost-items")
                        .queryParam("id", 분실물_1번_id.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer {access-token}")
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(분실물_1번_id.toString()))
                .andExpect(jsonPath("$.name").value(updateDto.getName()))
                .andExpect(jsonPath("$.description").value(updateDto.getDescription()))
                .andExpect(jsonPath("$.thumbnail").value(updateDto.getThumbnail()))
                .andExpect(jsonPath("$.findDate")
                        .value(updateDto.getFindDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .andDo(LostItemDoc.updateLostItem());
        verify(lostItemService, times(1)).update(anyLong(), any(LostItemReqDto.class));
    }

    @Test
    void 분실물_수정시_권한이_없는_경우() throws Exception {
        // when & then
        ResultActions result = mockMvc.perform(put("/lost-items")
                        .queryParam("id", 분실물_1번_id.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer {access-token}")
                        .content(objectMapper.writeValueAsString(lostItemReqDto)))
                .andExpect(status().isUnauthorized())
                .andDo(LostItemDoc.errorSnippet("분실물_수정_인증_거부"));
        assertError(result, "A002", "Unauthenticated", 401);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 분실물_삭제() throws Exception {
        // when & then
        mockMvc.perform(delete("/lost-items")
                        .queryParam("id", 분실물_1번_id.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer {access-token}"))
                .andExpect(status().isOk())
                .andDo(LostItemDoc.deleteLostItem());
        verify(lostItemService, times(1)).delete(anyLong());
    }

    @Test
    void 분실물_삭제시_권한인_없는_경우() throws Exception {
        // when & then
        ResultActions result = mockMvc.perform(delete("/lost-items")
                        .queryParam("id", 분실물_1번_id.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer {access-token}"))
                .andExpect(status().isUnauthorized())
                .andDo(LostItemDoc.errorSnippet("분실물_삭제시_인증_거부"));
        assertError(result, "A002", "Unauthenticated", 401);
    }

    @Test
    void 분실물_단일조회() throws Exception {
        // given
        LostItemResDto response = 분실물_1번_응답_DTO();
        given(lostItemService.getLostItem(분실물_1번_id)).willReturn(response);

        // when & then
        mockMvc.perform(get("/lost-items/{id}", 분실물_1번_id).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(response.getId().toString()))
                .andExpect(jsonPath("$.name").value(response.getName()))
                .andExpect(jsonPath("$.description").value(response.getDescription()))
                .andExpect(jsonPath("$.thumbnail").value(response.getThumbnail()))
                .andExpect(jsonPath("$.findDate")
                        .value(response.getFindDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .andDo(LostItemDoc.getLostItem());
        verify(lostItemService, times(1)).getLostItem(anyLong());
    }

    @Test
    void 분실물_전체_조회() throws Exception {
        // given
        LostItemSummaryDto lostItemSummaryDto1 = 분실물_1번_요약_DTO();
        LostItemSummaryDto lostItemSummaryDto2 = 분실물_2번_요약_DTO();
        given(lostItemService.getLostItemsByFestivalId(1L))
                .willReturn(List.of(lostItemSummaryDto1, lostItemSummaryDto2));

        // when & then
        mockMvc.perform(get("/{festivalId}/lost-items", 1L).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(lostItemSummaryDto1.getId().toString()))
                .andExpect(jsonPath("$[0].name").value(lostItemSummaryDto1.getName()))
                .andExpect(jsonPath("$[0].description").value(lostItemSummaryDto1.getDescription()))
                .andExpect(jsonPath("$[1].id").value(lostItemSummaryDto2.getId().toString()))
                .andExpect(jsonPath("$[1].name").value(lostItemSummaryDto2.getName()))
                .andExpect(jsonPath("$[1].description").value(lostItemSummaryDto2.getDescription()))
                .andDo(LostItemDoc.getLostItemsByFestival());
        verify(lostItemService, times(1)).getLostItemsByFestivalId(anyLong());
    }
}
