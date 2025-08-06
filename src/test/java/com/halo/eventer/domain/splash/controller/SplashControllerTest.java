package com.halo.eventer.domain.splash.controller;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.halo.eventer.domain.festival.FestivalFixture;
import com.halo.eventer.domain.splash.Splash;
import com.halo.eventer.domain.splash.api_docs.SplashDocs;
import com.halo.eventer.domain.splash.dto.request.ImageLayerDto;
import com.halo.eventer.domain.splash.dto.response.SplashGetDto;
import com.halo.eventer.domain.splash.service.SplashService;
import com.halo.eventer.global.config.ControllerTestSecurityBeans;
import com.halo.eventer.global.config.CustomRestDocsConfig;
import com.halo.eventer.global.config.security.SecurityConfig;
import com.halo.eventer.global.security.provider.JwtProvider;

import static com.halo.eventer.domain.splash.SplashFixture.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SplashController.class)
@ActiveProfiles("test")
@AutoConfigureRestDocs
@SuppressWarnings("NonAsciiCharacters")
@Import({ControllerTestSecurityBeans.class, SecurityConfig.class, CustomRestDocsConfig.class})
public class SplashControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SplashService splashService;

    @MockitoBean
    private JwtProvider jwtProvider;

    private List<ImageLayerDto> dto;
    private List<String> layerTypes;

    private Splash splash;

    @BeforeEach
    void setup() {
        dto = 이미지_업로드_DTO_생성();
        layerTypes = 이미지_삭제_타입_리스트();
        splash = 스플래시1_생성(FestivalFixture.축제_엔티티());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 스플레시_업로드() throws Exception {
        mockMvc.perform(post("/splash")
                        .param("festivalId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer {access-token}")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andDo(SplashDocs.uploadSplash());
    }

    @Test
    void 스플래시_업로드_권한검증_실패() throws Exception {
        mockMvc.perform(post("/splash")
                        .param("festivalId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isUnauthorized())
                .andDo(SplashDocs.errorSnippet("권한 검증 실패"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 스플레시_업로드_festivalId_검증_실패() throws Exception {
        mockMvc.perform(post("/splash")
                        .param("festivalId", "0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer {access-token}")
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("C013"))
                .andExpect(jsonPath("$.message").value("must be greater than or equal to 1"))
                .andExpect(jsonPath("$.status").value(400))
                .andDo(SplashDocs.errorSnippet("festivalId 위반"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 스플레시_업로드_url_검증_실패() throws Exception {
        ImageLayerDto errorDto = new ImageLayerDto();
        setField(errorDto, "url", "");
        setField(errorDto, "layerType", "background");

        mockMvc.perform(post("/splash")
                        .param("festivalId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer {access-token}")
                        .content(objectMapper.writeValueAsString(List.of(errorDto))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("C013"))
                .andExpect(jsonPath("$.message").value("url은 필수 값입니다."))
                .andExpect(jsonPath("$.status").value(400))
                .andDo(SplashDocs.errorSnippet("ImageLayerDto 위반 - url 누락"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 스플레시_업로드_layerType_검증_실패() throws Exception {
        ImageLayerDto errorDto = new ImageLayerDto();
        setField(errorDto, "url", "url.com");
        setField(errorDto, "layerType", "");

        mockMvc.perform(post("/splash")
                        .param("festivalId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer {access-token}")
                        .content(objectMapper.writeValueAsString(List.of(errorDto))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("C013"))
                .andExpect(jsonPath("$.message").value("layerType은 필수값입니다."))
                .andExpect(jsonPath("$.status").value(400))
                .andDo(SplashDocs.errorSnippet("ImageLayerDto 위반 - layerType 올바르지 않음"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 스플레시_삭제() throws Exception {
        mockMvc.perform(delete("/splash")
                        .param("festivalId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer {access-token}")
                        .content(objectMapper.writeValueAsString(layerTypes)))
                .andExpect(status().isOk())
                .andDo(SplashDocs.deleteSplash());
    }

    @Test
    void 스플래시_삭제_권한검증_실패() throws Exception {
        mockMvc.perform(delete("/splash")
                        .param("festivalId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(layerTypes)))
                .andExpect(status().isUnauthorized())
                .andDo(SplashDocs.errorSnippet("권한검증_실패"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 스플레시_삭제_festivalId_검증_실패() throws Exception {
        mockMvc.perform(delete("/splash")
                        .param("festivalId", "0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer {access-token}")
                        .content(objectMapper.writeValueAsString(layerTypes)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("C013"))
                .andExpect(jsonPath("$.message").value("must be greater than or equal to 1"))
                .andExpect(jsonPath("$.status").value(400))
                .andDo(SplashDocs.errorSnippet("festivalId 위반"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 스플레시_삭제_layerType_검증_실패() throws Exception {

        mockMvc.perform(delete("/splash")
                        .param("festivalId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer {access-token}")
                        .content(objectMapper.writeValueAsString(List.of(""))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("C013"))
                .andExpect(jsonPath("$.message").value("must not be blank"))
                .andExpect(jsonPath("$.status").value(400))
                .andDo(SplashDocs.errorSnippet("ImageLayerDto 위반 - layerType 올바르지 않음"));
    }

    @Test
    void 스플레시_조회() throws Exception {
        SplashGetDto expected = new SplashGetDto(
                splash.getBackgroundImage(),
                splash.getTopLayerImage(),
                splash.getCenterLayerImage(),
                splash.getBottomLayerImage());
        given(splashService.getSplash(anyLong())).willReturn(expected);

        // when & then
        mockMvc.perform(get("/splash").param("festivalId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.background").value(expected.getBackground()))
                .andExpect(jsonPath("$.top").value(expected.getTop()))
                .andExpect(jsonPath("$.center").value(expected.getCenter()))
                .andExpect(jsonPath("$.bottom").value(expected.getBottom()))
                .andDo(SplashDocs.getSplash());
    }

    @Test
    void 스플레시_조회_festivalId_검증_실패() throws Exception {
        mockMvc.perform(get("/splash").param("festivalId", "0")) // @Min(1) 위반
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("C013"))
                .andExpect(jsonPath("$.message").value("must be greater than or equal to 1"))
                .andExpect(jsonPath("$.status").value(400))
                .andDo(SplashDocs.errorSnippet("festivalId 위반"));
    }
}
