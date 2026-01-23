package com.halo.eventer.domain.stamp.controller;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
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
import com.halo.eventer.domain.stamp.api_docs.MissionDocs;
import com.halo.eventer.domain.stamp.dto.mission.MissionUpdateDto;
import com.halo.eventer.domain.stamp.dto.mission.request.MissionSetReqDto;
import com.halo.eventer.domain.stamp.dto.mission.response.MissionDetailGetResDto;
import com.halo.eventer.domain.stamp.dto.mission.response.MissionSummaryResDto;
import com.halo.eventer.domain.stamp.service.MissionService;
import com.halo.eventer.global.config.ControllerTestSecurityBeans;
import com.halo.eventer.global.config.CustomRestDocsConfig;
import com.halo.eventer.global.config.security.SecurityConfig;
import com.halo.eventer.global.security.provider.JwtProvider;

import static com.halo.eventer.domain.stamp.fixture.MissionFixture.*;
import static com.halo.eventer.domain.stamp.fixture.StampFixture.스탬프1_ID;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MissionController.class)
@SuppressWarnings("NonAsciiCharacters")
@ActiveProfiles("test")
@AutoConfigureRestDocs
@Import({ControllerTestSecurityBeans.class, SecurityConfig.class, CustomRestDocsConfig.class})
public class MissionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MissionService missionService;

    @MockitoBean
    private JwtProvider jwtProvider;

    private final String ADMIN_TOKEN = "Bearer admin-token";

    private List<MissionSetReqDto> missionSetDtos;
    private List<MissionSummaryResDto> missionSummaryGetDtos;
    private MissionDetailGetResDto missionDetailGetDto;
    private MissionUpdateDto missionUpdateDto;

    @BeforeEach
    void setUp() {
        // Fixture 사용
        missionSetDtos = 미션_셋업_리스트();
        missionSummaryGetDtos = 미션_조회_리스트_생성();
        missionDetailGetDto = new MissionDetailGetResDto(미션1_ID, 미션1_제목, 미션1_내용, 미션1_장소, 미션1_시간, 미션1_성공사진, 미션1_실패사진);
        missionUpdateDto = 미션_업데이트_DTO_생성();
    }

    @Nested
    class 미션_목록_생성 {

        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        void 미션_목록_생성_성공() throws Exception {
            doNothing().when(missionService).createMission(anyLong(), any());
            mockMvc.perform(post("/stamp/mission")
                            .queryParam("stampId", "1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, ADMIN_TOKEN)
                            .content(objectMapper.writeValueAsString(missionSetDtos)))
                    .andExpect(status().isOk())
                    .andDo(MissionDocs.createMissionList());
        }

        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        void 미션_목록_생성_stampId_검증_실패() throws Exception {
            mockMvc.perform(post("/stamp/mission")
                            .queryParam("stampId", "0")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, ADMIN_TOKEN)
                            .content(objectMapper.writeValueAsString(missionSetDtos)))
                    .andExpect(status().isBadRequest())
                    .andDo(MissionDocs.errorSnippet("미션_목록_생성_stampId_검증_실패"));
        }

        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        void 미션_목록_생성_요청본문_검증_실패_리스트비어있음_실패() throws Exception {
            mockMvc.perform(post("/stamp/mission")
                            .queryParam("stampId", "1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, ADMIN_TOKEN)
                            .content("[]"))
                    .andExpect(status().isBadRequest())
                    .andDo(MissionDocs.errorSnippet("미션_목록_생성_요청본문_검증_실패_리스트비어있음_실패"));
        }

        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        void 미션_목록_생성_요청본문_검증_실패_필드누락_실패() throws Exception {
            var invalidItem = Map.of("boothId", 1);
            var body = List.of(invalidItem);
            mockMvc.perform(post("/stamp/mission")
                            .queryParam("stampId", "1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, ADMIN_TOKEN)
                            .content(objectMapper.writeValueAsString(body)))
                    .andExpect(status().isBadRequest())
                    .andDo(MissionDocs.errorSnippet("미션_목록_생성_요청본문_검증_실패_필드누락_실패"));
        }

        //        @Test
        //        void 미션_목록_생성_권한없음_실패() throws Exception {
        //            mockMvc.perform(post("/stamp/mission")
        //                            .queryParam("stampId", "1")
        //                            .contentType(MediaType.APPLICATION_JSON)
        //                            .accept(MediaType.APPLICATION_JSON)
        //                            .header(HttpHeaders.AUTHORIZATION, ADMIN_TOKEN)
        //                            .content(objectMapper.writeValueAsString(missionSetDtos)))
        //                    .andExpect(status().isUnauthorized())
        //                    .andDo(MissionDocs.errorSnippet("미션_목록_생성_권한없음_실패"));
        //        }
    }

    @Nested
    class 미션_단건_조회 {

        @Test
        void 미션_단건_조회_성공() throws Exception {
            given(missionService.getMission(미션1_ID)).willReturn(missionDetailGetDto);

            mockMvc.perform(get("/stamp/mission")
                            .queryParam("missionId", String.valueOf(미션1_ID))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.boothId").value(미션1_부스_ID))
                    .andExpect(jsonPath("$.title").value(미션1_제목))
                    .andExpect(jsonPath("$.content").value(미션1_내용))
                    .andExpect(jsonPath("$.place").value(미션1_장소))
                    .andExpect(jsonPath("$.time").value(미션1_시간))
                    .andExpect(jsonPath("$.clearedThumbnail").value(미션1_성공사진))
                    .andExpect(jsonPath("$.notClearedThumbnail").value(미션1_실패사진))
                    .andDo(MissionDocs.getMission());
        }

        @Test
        void 미션_단건_조회_missionId_검증_실패() throws Exception {
            mockMvc.perform(get("/stamp/mission").queryParam("missionId", "0").accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andDo(MissionDocs.errorSnippet("미션_단건_조회_missionId_검증_실패"));
        }
    }

    @Nested
    class 미션_목록_조회 {

        @Test
        void 미션_목록_조회_성공() throws Exception {
            given(missionService.getMissions(스탬프1_ID)).willReturn(missionSummaryGetDtos);

            mockMvc.perform(get("/stamp/mission/all")
                            .queryParam("stampId", String.valueOf(스탬프1_ID))
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].missionId").value(미션1_ID))
                    .andExpect(jsonPath("$[0].title").value(미션1_제목))
                    .andExpect(jsonPath("$[0].clearedThumbnail").value(미션1_성공사진))
                    .andExpect(jsonPath("$[0].notClearedThumbnail").value(미션1_실패사진))
                    .andExpect(jsonPath("$[1].missionId").value(미션2_ID))
                    .andExpect(jsonPath("$[1].title").value(미션2_제목))
                    .andExpect(jsonPath("$[1].clearedThumbnail").value(미션2_성공사진))
                    .andExpect(jsonPath("$[1].notClearedThumbnail").value(미션2_실패사진))
                    .andDo(MissionDocs.getMissionList());
        }

        @Test
        void 미션_목록_조회_stampId_검증_실패() throws Exception {
            mockMvc.perform(get("/stamp/mission/all").queryParam("stampId", "0").accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andDo(MissionDocs.errorSnippet("미션_목록_조회_stampId_검증_실패"));
        }
    }

    @Nested
    class 미션_수정 {

        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        void 미션_수정_성공() throws Exception {
            doNothing().when(missionService).updateMission(anyLong(), any(MissionUpdateDto.class));

            mockMvc.perform(patch("/stamp/mission")
                            .queryParam("missionId", String.valueOf(미션1_ID))
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, ADMIN_TOKEN)
                            .content(objectMapper.writeValueAsString(missionUpdateDto)))
                    .andExpect(status().isOk())
                    .andDo(MissionDocs.updateMission());
        }

        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        void 미션_수정_missionId_검증_실패() throws Exception {
            mockMvc.perform(patch("/stamp/mission")
                            .queryParam("missionId", "0")
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, ADMIN_TOKEN)
                            .content(objectMapper.writeValueAsString(missionUpdateDto)))
                    .andExpect(status().isBadRequest())
                    .andDo(MissionDocs.errorSnippet("미션_수정_missionId_검증_실패"));
        }

        //        @Test
        //        // TODO : 미션 수정 api가 권한이 없어도 수정을 허용함. 확인 필요
        //        void 미션_수정_권한없음_실패() throws Exception {
        //            mockMvc.perform(patch("/stamp/mission")
        //                            .queryParam("missionId", String.valueOf(미션1_ID))
        //                            .contentType(MediaType.APPLICATION_JSON)
        //                            .header(HttpHeaders.AUTHORIZATION, ADMIN_TOKEN)
        //                            .content(objectMapper.writeValueAsString(missionUpdateDto)))
        //                    .andExpect(status().isUnauthorized())
        //                    .andDo(MissionDocs.errorSnippet("미션_수정_권한없음_실패"));
        //        }
    }
}
