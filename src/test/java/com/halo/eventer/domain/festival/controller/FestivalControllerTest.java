package com.halo.eventer.domain.festival.controller;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.halo.eventer.domain.festival.dto.*;
import com.halo.eventer.domain.festival.service.FestivalService;
import com.halo.eventer.domain.member.Member;
import com.halo.eventer.global.config.ControllerTestSecurityBeans;
import com.halo.eventer.global.config.security.SecurityConfig;
import com.halo.eventer.global.security.CustomUserDetails;
import com.halo.eventer.global.security.provider.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(FestivalController.class)
@AutoConfigureMockMvc
@SuppressWarnings("NonAsciiCharacters")
@ActiveProfiles("test")
@Import({ControllerTestSecurityBeans.class, SecurityConfig.class})
public class FestivalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FestivalService festivalService;

    @MockBean
    private JwtProvider jwtProvider;


    private final String ADMIN_TOKEN = "Bearer admin-token";
    private FestivalCreateDto festivalCreateDto;
    private FestivalResDto festivalResDto;
    private ColorDto colorDto;

    @BeforeEach
    void setAll(){
        colorDto = new ColorDto("#FFFFFF", "#000000", "#CCCCCC", "#F0F0F0");
        festivalCreateDto =  new FestivalCreateDto("축제","univ");
        festivalResDto = new FestivalResDto(1L,"축제","logo",colorDto,
                127.123456,123.123456);
    }
    @Nested
    class 어드민_테스트{
        @BeforeEach
        void setUp() {
            colorDto = new ColorDto("#FFFFFF", "#000000", "#CCCCCC", "#F0F0F0");
            festivalCreateDto =  new FestivalCreateDto("축제","univ");
            festivalResDto = new FestivalResDto(1L,"축제","logo",colorDto,
                    127.123456,123.123456);
            CustomUserDetails customUserDetails = new CustomUserDetails(new Member("admin", "1234"));
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    customUserDetails, null,
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_ADMIN"))
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        @Test
        void ADMIN_축제생성()throws Exception{
            //given
            doNothing().when(festivalService).create(any(FestivalCreateDto.class));

            //when & then
            mockMvc.perform(post("/festival")
                            .header("Authorization", ADMIN_TOKEN)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(festivalCreateDto)))
                    .andExpect(status().isOk());
        }

        @Test
        void ADMIN_축제수정() throws Exception{
            //given
            given(festivalService.update(any(),any())).willReturn(festivalResDto);

            // when & then
            mockMvc.perform(patch("/festival/{id}", 1L)
                            .header("Authorization", ADMIN_TOKEN)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(festivalCreateDto)))
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(festivalResDto)));
        }

        @Test
        void ADMIN_축제삭제() throws Exception{
            //given
            doNothing().when(festivalService).delete(1L);

            // when & then
            mockMvc.perform(delete("/festival/{id}", 1L)
                            .header("Authorization", ADMIN_TOKEN)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

        @Test
        void ADMIN_색수정() throws Exception{
            doNothing().when(festivalService).updateColor(1L,colorDto);

            // when & then
            mockMvc.perform(post("/festival/{festivalId}/color", 1L)
                            .header("Authorization", ADMIN_TOKEN)
                            .content(objectMapper.writeValueAsString(festivalCreateDto))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

        @Test
        void ADMIN_위치정보_수정()throws Exception{
            //given
            FestivalLocationDto festivalLocationDto = new FestivalLocationDto();
            given(festivalService.updateLocation(any(),any())).willReturn(festivalResDto);

            // when & then
            mockMvc.perform(post("/festival/{festivalId}/location", 1L)
                            .header("Authorization", ADMIN_TOKEN)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(festivalCreateDto)))
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(festivalResDto)));
        }
    }

    @Nested
    class 일반유저_테스트{

        @Test
        void 토큰없는유저_축제생성_실패()throws Exception{
            FestivalCreateDto dto = new FestivalCreateDto("축제","univ");
            doNothing().when(festivalService).create(any(FestivalCreateDto.class));

            mockMvc.perform(post("/festival")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void 토큰없는유저_축제수정_실패()throws Exception{
            //given
            given(festivalService.update(any(),any())).willReturn(festivalResDto);

            // when & then
            mockMvc.perform(patch("/festival/{id}", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(festivalCreateDto)))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void 토큰없는유저_축제삭제_실패()throws Exception{
            //given
            doNothing().when(festivalService).delete(1L);

            // when & then
            mockMvc.perform(delete("/festival/{id}", 1L)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void 토큰없는유저_축제색수정_실패() throws Exception{
            doNothing().when(festivalService).updateColor(1L,colorDto);

            // when & then
            mockMvc.perform(post("/festival/{festivalId}/color", 1L)
                            .content(objectMapper.writeValueAsString(festivalCreateDto))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void 토큰없는유저_위치정보_수정_실패()throws Exception{
            //given
            FestivalLocationDto festivalLocationDto = new FestivalLocationDto();
            given(festivalService.updateLocation(any(),any())).willReturn(festivalResDto);

            // when & then
            mockMvc.perform(post("/festival/{festivalId}/location", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(festivalCreateDto)))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    class 공통기능_테스트{
        @Test
        void 축제_정보_조회()throws Exception{
            //given
            given(festivalService.findById(any())).willReturn(festivalResDto);

            //then
            mockMvc.perform(get("/festival/{id}",1L)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(festivalResDto)));
        }

        @Test
        void 축제_리스트_조회()throws Exception{
            //given
            FestivalListDto festivalListDto = new FestivalListDto(1L,"축제","univ",123,123);
            given(festivalService.findAll()).willReturn(List.of(festivalListDto));

            //then
            mockMvc.perform(get("/festival")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(1))
                    .andExpect(jsonPath("$[0].id").value(festivalListDto.getId()))
                    .andExpect(jsonPath("$[0].festivalName").value(festivalListDto.getFestivalName()))
                    .andExpect(jsonPath("$[0].subAddress").value(festivalListDto.getSubAddress()))
                    .andExpect(jsonPath("$[0].latitude").value(festivalListDto.getLatitude()))
                    .andExpect(jsonPath("$[0].longitude").value(festivalListDto.getLongitude()));
        }
    }
}
