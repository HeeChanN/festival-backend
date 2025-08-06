package com.halo.eventer.domain.home.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.halo.eventer.domain.festival.FestivalFixture;
import com.halo.eventer.domain.festival.dto.FestivalSummaryDto;
import com.halo.eventer.domain.festival.service.FestivalService;
import com.halo.eventer.domain.home.HomeFixture;
import com.halo.eventer.domain.home.api_docs.HomeDoc;
import com.halo.eventer.domain.home.dto.HomeDto;
import com.halo.eventer.domain.home.service.HomeService;
import com.halo.eventer.global.config.ControllerTestSecurityBeans;
import com.halo.eventer.global.config.security.SecurityConfig;
import com.halo.eventer.global.security.provider.JwtProvider;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HomeController.class)
@AutoConfigureRestDocs
@SuppressWarnings("NonAsciiCharacters")
@ActiveProfiles("test")
@Import({ControllerTestSecurityBeans.class, SecurityConfig.class})
public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private HomeService homeService;

    @MockitoBean
    private FestivalService festivalService;

    @MockitoBean
    private JwtProvider jwtProvider;

    @Test
    void 메인페이지_조회() throws Exception {
        // given
        HomeDto homeDto = HomeFixture.메인화면_응답();
        given(homeService.getMainPage(anyLong())).willReturn(homeDto);

        // when & then
        mockMvc.perform(get("/home/{festivalId}", 1L).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(homeDto)))
                .andDo(HomeDoc.getMainPage());
    }

    @Test
    void 서브도메인기반_축제요약정보_조회() throws Exception {
        // given
        FestivalSummaryDto response = FestivalFixture.축제요약_응답_DTO();
        given(festivalService.findBySubAddress(anyString())).willReturn(response);

        // when & then
        mockMvc.perform(get("/home").queryParam("subAddress", "subAddress").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)))
                .andDo(HomeDoc.getFestivalSummaryBySubdomain());
    }

    @Test
    void 헬스체크() throws Exception {
        // when & then
        mockMvc.perform(get("/")).andExpect(status().isOk()).andDo(HomeDoc.healthCheck());
    }
}
