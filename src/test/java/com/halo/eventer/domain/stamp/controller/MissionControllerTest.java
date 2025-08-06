package com.halo.eventer.domain.stamp.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.halo.eventer.domain.stamp.Mission;
import com.halo.eventer.domain.stamp.dto.mission.MissionDetailGetDto;
import com.halo.eventer.domain.stamp.dto.mission.MissionUpdateDto;
import com.halo.eventer.domain.stamp.exception.MissionNotFoundException;
import com.halo.eventer.domain.stamp.service.MissionService;
import com.halo.eventer.global.error.ErrorResponse;
import com.halo.eventer.global.error.GlobalExceptionHandler;
import com.halo.eventer.global.error.exception.BaseException;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MissionController.class)
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class MissionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MissionService missionService;

    private static final String missionEndpoint = "/stamp/mission";

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new MissionController(missionService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void 미션_단일_조회_성공() throws Exception {
        // given
        Mission mission = setUpMission();
        MissionDetailGetDto response = MissionDetailGetDto.from(mission);

        // when
        when(missionService.getMission(anyLong())).thenReturn(MissionDetailGetDto.from(mission));

        // then
        mockMvc.perform(get(missionEndpoint).param("missionId", String.valueOf(anyLong())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.boothId").value(response.getBoothId()));
    }

    @Test
    void 미션_단일_조회_실패() throws Exception {
        // given
        BaseException exception = new MissionNotFoundException(1L);
        ErrorResponse errorResponse = ErrorResponse.of(exception.getMessage(), exception.getErrorCode());

        // when
        when(missionService.getMission(anyLong())).thenThrow(new MissionNotFoundException(1L));
        ResultActions result = mockMvc.perform(get(missionEndpoint).param("missionId", "1"));

        // then
        result.andExpect(status().is4xxClientError());
        result.andExpect(jsonPath("$.message").value(errorResponse.getMessage()));
    }

    @Test
    void 미션_수정_성공() throws Exception {
        // given
        MissionUpdateDto dto = setUpMissionUpdateDto();

        // when
        doNothing().when(missionService).updateMission(1L, dto);
        ResultActions result = mockMvc.perform(patch(missionEndpoint)
                .param("missionId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));
        // then
        result.andExpect(status().isOk());
    }

    private Mission setUpMission() {
        Mission mission = new Mission();
        setField(mission, "boothId", 1L);
        return mission;
    }

    private MissionUpdateDto setUpMissionUpdateDto() {
        MissionUpdateDto dto = new MissionUpdateDto();
        setField(dto, "boothId", 1L);
        return dto;
    }
}
