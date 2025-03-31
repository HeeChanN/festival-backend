package com.halo.eventer.domain.duration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.halo.eventer.domain.duration.dto.DurationCreateDto;
import com.halo.eventer.domain.duration.dto.DurationResDto;
import com.halo.eventer.domain.duration.service.DurationService;
import com.halo.eventer.global.config.ControllerTestSecurityBeans;
import com.halo.eventer.global.config.security.SecurityConfig;
import com.halo.eventer.global.security.provider.JwtProvider;
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

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DurationController.class)
@AutoConfigureMockMvc
@SuppressWarnings("NonAsciiCharacters")
@ActiveProfiles("test")
@Import({ControllerTestSecurityBeans.class, SecurityConfig.class})
public class DurationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired private ObjectMapper objectMapper;

    @MockBean
    private DurationService durationService;

    @MockBean private JwtProvider jwtProvider;

    private final Long festivalId = 1L;
    List<DurationCreateDto> durationCreateDtos;
    List<DurationResDto> durationResDtos;

  @BeforeEach
  void setUp() {
    durationCreateDtos = List.of(
            new DurationCreateDto(LocalDate.of(2025, 3, 1), 3),
            new DurationCreateDto(LocalDate.of(2025, 3, 5), 2));
    durationResDtos = List.of(
            new DurationResDto()
    );
  }

  @Test
  @WithMockUser(username = "admin", roles = "ADMIN")
  void 어드민_축제기간_생성() throws Exception {
    // given
    doNothing().when(durationService).createDurations(any(), any());

    // when & then
    mockMvc.perform(post("/duration")
                .param("festivalId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(durationCreateDtos)))
            .andExpect(status().isOk());
    verify(durationService, times(1)).createDurations(any(), any());
  }

  @Test
  void 일반유저_축제기간_생성_인증거부() throws Exception {
      // given
      doNothing().when(durationService).createDurations(any(), any());

      // when & then
      mockMvc.perform(post("/duration")
                      .param("festivalId", "1")
                      .contentType(MediaType.APPLICATION_JSON)
                      .content(objectMapper.writeValueAsString(durationCreateDtos)))
              .andExpect(status().isUnauthorized());
  }

  @Test
    void 축제기간_리스트_조회() throws Exception {
      //given
      given(durationService.getDurations(any())).willReturn(durationResDtos);

      // when & then
      mockMvc.perform(get("/duration")
                      .param("festivalId", "1")
                      .contentType(MediaType.APPLICATION_JSON))
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.length()").value(1));
  }
}
