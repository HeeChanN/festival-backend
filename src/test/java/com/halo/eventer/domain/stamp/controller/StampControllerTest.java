package com.halo.eventer.domain.stamp.controller;

import java.util.List;

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
import com.halo.eventer.domain.stamp.api_docs.StampDocs;
import com.halo.eventer.domain.stamp.dto.mission.response.MissionSummaryResDto;
import com.halo.eventer.domain.stamp.dto.stamp.StampGetDto;
import com.halo.eventer.domain.stamp.dto.stamp.StampUsersGetDto;
import com.halo.eventer.domain.stamp.service.StampService;
import com.halo.eventer.global.config.ControllerTestSecurityBeans;
import com.halo.eventer.global.config.CustomRestDocsConfig;
import com.halo.eventer.global.config.security.SecurityConfig;
import com.halo.eventer.global.security.provider.JwtProvider;

import static com.halo.eventer.domain.stamp.fixture.MissionFixture.*;
import static com.halo.eventer.domain.stamp.fixture.StampFixture.*;
import static com.halo.eventer.domain.stamp.fixture.StampUserFixture.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StampController.class)
@SuppressWarnings("NonAsciiCharacters")
@ActiveProfiles("test")
@AutoConfigureRestDocs
@Import({ControllerTestSecurityBeans.class, SecurityConfig.class, CustomRestDocsConfig.class})
public class StampControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private StampService stampService;

    @MockitoBean
    private JwtProvider jwtProvider;

    private final Long 축제_1번 = 1L;

    private List<StampGetDto> stampGetDtos;
    private List<MissionSummaryResDto> missionSummaryGetDtos;
    private List<StampUsersGetDto> stampUsersGetDtos;

    private final String ADMIN_TOKEN = "Bearer admin-token";

    @BeforeEach
    void setUp() {
        stampGetDtos = 스탬프_목록_DTO_생성();
        missionSummaryGetDtos = 미션_조회_리스트_생성();
        stampUsersGetDtos = 스탬프유저들_응답_DTO_생성();
    }

    @Nested
    class Stamp_등록 {
        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        void 스탬프_등록() throws Exception {
            given(stampService.registerStamp(축제_1번)).willReturn(stampGetDtos);
            mockMvc.perform(post("/stamp")
                            .queryParam("festivalId", "1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, ADMIN_TOKEN))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].stampId").value(스탬프1_ID))
                    .andExpect(jsonPath("$[0].stampOn").value(스탬프1_활성화))
                    .andExpect(jsonPath("$[0].stampFinishCnt").value(스탬프1_완료_카운트))
                    .andExpect(jsonPath("$[1].stampId").value(스탬프2_ID))
                    .andExpect(jsonPath("$[1].stampOn").value(스탬프2_활성화))
                    .andExpect(jsonPath("$[1].stampFinishCnt").value(스탬프2_완료_카운트))
                    .andExpect(jsonPath("$[2].stampId").value(스탬프3_ID))
                    .andExpect(jsonPath("$[2].stampOn").value(스탬프3_활성화))
                    .andExpect(jsonPath("$[2].stampFinishCnt").value(스탬프3_완료_카운트))
                    .andDo(StampDocs.registerStamp());
        }

        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        void 스탬프_등록_축제Id_검증_실패() throws Exception {
            mockMvc.perform(post("/stamp")
                            .queryParam("festivalId", "0")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, ADMIN_TOKEN))
                    .andExpect(status().isBadRequest())
                    .andDo(StampDocs.errorSnippet("스탬프_등록_축제Id_검증_실패"));
        }

        @Test
        void 스탬프_등록_권한없음_실패() throws Exception {
            mockMvc.perform(post("/stamp").param("festivalId", "1"))
                    .andExpect(status().isUnauthorized())
                    .andDo(StampDocs.errorSnippet("스탬프_등록_권한없음_실패"));
        }
    }

    @Nested
    class Stamp_조회 {
        @Test
        void 스탬프_조회() throws Exception {
            given(stampService.getStampByFestivalId(축제_1번)).willReturn(stampGetDtos);
            mockMvc.perform(get("/stamp")
                            .queryParam("festivalId", "1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, ADMIN_TOKEN))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].stampId").value(스탬프1_ID))
                    .andExpect(jsonPath("$[0].stampOn").value(스탬프1_활성화))
                    .andExpect(jsonPath("$[0].stampFinishCnt").value(스탬프1_완료_카운트))
                    .andExpect(jsonPath("$[1].stampId").value(스탬프2_ID))
                    .andExpect(jsonPath("$[1].stampOn").value(스탬프2_활성화))
                    .andExpect(jsonPath("$[1].stampFinishCnt").value(스탬프2_완료_카운트))
                    .andExpect(jsonPath("$[2].stampId").value(스탬프3_ID))
                    .andExpect(jsonPath("$[2].stampOn").value(스탬프3_활성화))
                    .andExpect(jsonPath("$[2].stampFinishCnt").value(스탬프3_완료_카운트))
                    .andDo(StampDocs.getStampList());
        }

        @Test
        void 스탬프_조회_축제Id_검증_실패() throws Exception {
            mockMvc.perform(get("/stamp")
                            .queryParam("festivalId", "0")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, ADMIN_TOKEN))
                    .andExpect(status().isBadRequest())
                    .andDo(StampDocs.errorSnippet("스탬프_조회_축제Id_검증_실패"));
        }
    }

    @Nested
    class Stamp_활성화 {
        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        void 스탬프_활성화() throws Exception {
            mockMvc.perform(patch("/stamp")
                            .queryParam("stampId", "1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, ADMIN_TOKEN))
                    .andExpect(status().isOk())
                    .andDo(StampDocs.updateStampOn());
        }

        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        void 스탬프_활성화_스탬프Id_검증_실패() throws Exception {
            mockMvc.perform(patch("/stamp")
                            .queryParam("stampId", "0")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, ADMIN_TOKEN))
                    .andExpect(status().isBadRequest())
                    .andDo(StampDocs.errorSnippet("스탬프_활성화_스탬프Id_검증_실패"));
        }

        @Test
        void 스탬프_활성화_권한_없음_실패() throws Exception {
            mockMvc.perform(patch("/stamp")
                            .queryParam("stampId", "1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, ADMIN_TOKEN))
                    .andExpect(status().isUnauthorized())
                    .andDo(StampDocs.errorSnippet("스탬프_활성화_권한_없음_실패"));
        }
    }

    @Nested
    class Stamp_삭제 {
        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        void 스탬프_삭제() throws Exception {
            mockMvc.perform(delete("/stamp")
                            .queryParam("stampId", "1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, ADMIN_TOKEN))
                    .andExpect(status().isOk())
                    .andDo(StampDocs.deleteStamp());
        }

        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        void 스탬프_삭제_스탬프Id_검증_실패() throws Exception {
            mockMvc.perform(delete("/stamp").queryParam("stampId", "0"))
                    .andExpect(status().isBadRequest())
                    .andDo(StampDocs.errorSnippet("스탬프_삭제_스탬프Id_검증_실패"));
        }

        @Test
        void 스탬프_삭제_권한_없음_실패() throws Exception {
            mockMvc.perform(delete("/stamp").queryParam("stampId", "1"))
                    .andExpect(status().isUnauthorized())
                    .andDo(StampDocs.errorSnippet("스탬프_삭제_권한_없음_실패"));
        }
    }

    @Nested
    class 미션_조회 {
        @Test
        void 미션_목록_조회() throws Exception {
            given(stampService.getMissions(스탬프1_ID)).willReturn(missionSummaryGetDtos);
            mockMvc.perform(get("/stamp/missions")
                            .queryParam("stampId", "1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, ADMIN_TOKEN))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].missionId").value(미션1_ID))
                    .andExpect(jsonPath("$[0].title").value(미션1_제목))
                    .andExpect(jsonPath("$[0].clearedThumbnail").value(미션1_성공사진))
                    .andExpect(jsonPath("$[0].notClearedThumbnail").value(미션1_실패사진))
                    .andExpect(jsonPath("$[1].missionId").value(미션2_ID))
                    .andExpect(jsonPath("$[1].title").value(미션2_제목))
                    .andExpect(jsonPath("$[1].clearedThumbnail").value(미션2_성공사진))
                    .andExpect(jsonPath("$[1].notClearedThumbnail").value(미션2_실패사진))
                    .andDo(StampDocs.getMissionList());
        }

        @Test
        void 미션_목록_조회_스탬프Id_검증_실패() throws Exception {
            mockMvc.perform(get("/stamp/missions")
                            .queryParam("stampId", "0")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, ADMIN_TOKEN))
                    .andExpect(status().isBadRequest())
                    .andDo(StampDocs.errorSnippet("미션_목록_조회_스탬프Id_검증_실패"));
        }
    }

    @Nested
    class 스탬프_참가자_조회 {
        @Test
        void 스탬프_참가자_조회_성공() throws Exception {
            given(stampService.getStampUsers(스탬프1_ID)).willReturn(stampUsersGetDtos);
            mockMvc.perform(get("/stamp/users")
                            .queryParam("stampId", String.valueOf(스탬프1_ID))
                            .contentType(MediaType.APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, ADMIN_TOKEN))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].uuid").value(유저1_UUID))
                    .andExpect(jsonPath("$[0].name").value(유저1_이름))
                    .andExpect(jsonPath("$[0].phone").value(유저1_전번))
                    .andExpect(jsonPath("$[0].finished").value(유저1_완료여부))
                    .andExpect(jsonPath("$[0].participantCount").value(유저1_참여_갯수))
                    .andExpect(jsonPath("$[1].uuid").value(유저2_UUID))
                    .andExpect(jsonPath("$[1].name").value(유저2_이름))
                    .andExpect(jsonPath("$[1].phone").value(유저2_전번))
                    .andExpect(jsonPath("$[1].finished").value(유저2_완료여부))
                    .andExpect(jsonPath("$[1].participantCount").value(유저2_참여_갯수))
                    .andDo(StampDocs.getStampUsers());
        }

        @Test
        void 스탬프_참가자_조회_스탬프Id_검증_실패() throws Exception {
            mockMvc.perform(get("/stamp/users")
                            .queryParam("stampId", "0")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, ADMIN_TOKEN))
                    .andExpect(status().isBadRequest())
                    .andDo(StampDocs.errorSnippet("스탬프_참가자_조회_스탬프Id_검증_실패"));
        }
    }

    @Nested
    class 완료_카운트_설정 {
        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        void 완료_카운트_설정_성공() throws Exception {
            mockMvc.perform(post("/stamp/finishCnt")
                            .queryParam("stampId", "1")
                            .queryParam("cnt", "5")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, ADMIN_TOKEN))
                    .andExpect(status().isOk())
                    .andDo(StampDocs.setFinishCnt());
        }

        @Test
        @WithMockUser(username = "admin", roles = "ADMIN")
        void 완료_카운트_설정_스탬프Id_count_검증_실패() throws Exception {
            mockMvc.perform(post("/stamp/finishCnt")
                            .queryParam("stampId", "0")
                            .queryParam("cnt", "0")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, ADMIN_TOKEN))
                    .andExpect(status().isBadRequest())
                    .andDo(StampDocs.errorSnippet("완료_카운트_설정_스탬프Id_count_검증_실패"));
        }

        @Test
        void 완료_카운트_설정_권한_없음_실패() throws Exception {
            mockMvc.perform(post("/stamp/finishCnt")
                            .queryParam("stampId", "1")
                            .queryParam("cnt", "5")
                            .contentType(MediaType.APPLICATION_JSON)
                            .header(HttpHeaders.AUTHORIZATION, ADMIN_TOKEN))
                    .andExpect(status().isUnauthorized())
                    .andDo(StampDocs.errorSnippet("완료_카운트_설정_권한_없음_실패"));
        }
    }
}
