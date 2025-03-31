package com.halo.eventer.domain.stamp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.dto.FestivalCreateDto;
import com.halo.eventer.domain.stamp.Stamp;
import com.halo.eventer.global.config.ControllerTestSecurityBeans;
import com.halo.eventer.global.security.WithMockCustomUserDetails;
import com.halo.eventer.domain.stamp.dto.stamp.*;
import com.halo.eventer.domain.stamp.service.StampService;
import com.halo.eventer.global.config.security.SecurityConfig;
import com.halo.eventer.global.security.provider.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SuppressWarnings("NonAsciiCharacters")
@ActiveProfiles("test")
@WebMvcTest(controllers = StampController.class)
@Import({ControllerTestSecurityBeans.class, SecurityConfig.class})
public class StampControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StampService stampService;

    @MockBean
    private JwtProvider jwtProvider;

    private Stamp stamp;

    private final String ADMIN_TOKEN = "Bearer admin-token";

    @BeforeEach
    void setUp() {
        stamp = Stamp.create(Festival.from(
                new FestivalCreateDto("test festival","test")
        ));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 스탬프_생성_성공() throws Exception {
        given(stampService.registerStamp(anyLong()))
                .willReturn(List.of(StampGetDto.from(stamp)));

        mockMvc.perform(post("/stamp")
                        .param("festivalId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void 스탬프_조회_성공() throws Exception {
        given(stampService.getStampByFestivalId(anyLong()))
                .willReturn(List.of(StampGetDto.from(stamp)));

        mockMvc.perform(get("/stamp")
                        .param("festivalId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 스탬프_상태변경_성공() throws Exception {
        mockMvc.perform(patch("/stamp")
                        .header("Authorization", ADMIN_TOKEN)
                        .param("stampId", "1"))
                .andExpect(status().isOk());
        then(stampService).should().updateStampOn(1L);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 스탬프_삭제_성공() throws Exception {
        mockMvc.perform(delete("/stamp")
                        .header("Authorization", ADMIN_TOKEN)
                        .param("stampId", "1"))
                .andExpect(status().isOk());
        then(stampService).should().deleteStamp(1L);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 미션_생성_성공() throws Exception {
        MissionSetListDto dto = new MissionSetListDto();
        mockMvc.perform(post("/stamp/mission")
                        .header("Authorization", ADMIN_TOKEN)
                        .param("stampId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isOk());
        then(stampService).should().createMission(anyLong(), any());
    }

    @Test
    void 미션_리스트_조회_성공() throws Exception {
        given(stampService.getMissions(anyLong()))
                .willReturn(List.of(new MissionSummaryGetDto()));

        mockMvc.perform(get("/stamp/missions")
                        .param("stampId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void 유저_리스트_조회_성공() throws Exception {
        given(stampService.getStampUsers(anyLong()))
                .willReturn(List.of(new StampUsersGetDto()));

        mockMvc.perform(get("/stamp/users")
                        .param("stampId", "1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void 완료_기준_설정_성공() throws Exception {
        mockMvc.perform(post("/stamp/finishCnt")
                        .header("Authorization", ADMIN_TOKEN)
                        .param("stampId", "1")
                        .param("cnt", "3"))
                .andExpect(status().isOk());
        then(stampService).should().setFinishCnt(1L, 3);
    }
}
