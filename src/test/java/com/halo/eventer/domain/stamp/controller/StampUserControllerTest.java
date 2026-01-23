package com.halo.eventer.domain.stamp.controller;

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
import com.halo.eventer.domain.stamp.api_docs.StampUserDocs;
import com.halo.eventer.domain.stamp.dto.stamp.StampUsersGetDto;
import com.halo.eventer.domain.stamp.dto.stampUser.*;
import com.halo.eventer.domain.stamp.service.StampUserService;
import com.halo.eventer.global.config.ControllerTestSecurityBeans;
import com.halo.eventer.global.config.CustomRestDocsConfig;
import com.halo.eventer.global.config.security.SecurityConfig;
import com.halo.eventer.global.security.provider.JwtProvider;

import static com.halo.eventer.domain.stamp.fixture.StampUserFixture.*;
import static com.halo.eventer.domain.stamp.fixture.UserMissionFixture.유저미션_완료여부_응답_생성;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StampUserController.class)
@ActiveProfiles("test")
@AutoConfigureRestDocs
@SuppressWarnings("NonAsciiCharacters")
@Import({ControllerTestSecurityBeans.class, SecurityConfig.class, CustomRestDocsConfig.class})
class StampUserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private StampUserService stampUserService;

    @MockitoBean
    private JwtProvider jwtProvider;

    private SignupWithoutCustomDto signupWithoutCustomDto;
    private SignupWithCustomDto signupWithCustomDto;
    private SignupDto signupDto;
    private LoginDto loginDto;
    private StampUserGetDto stampUserGetDto;
    private UserMissionInfoWithFinishedGetListDto userMissionInfoWithFinishedGetListDto;

    private final String ADMIN_TOKEN = "Bearer admin-token";

    @BeforeEach
    void setUp() {
        signupWithoutCustomDto = 커스텀_없는_회원가입_DTO_생성();
        signupWithCustomDto = 커스텀_있는_회원가입_DTO_생성();
        signupDto = 회원가입_DTO_생성();
        loginDto = 로그인_DTO_생성();
        stampUserGetDto = 스탬프유저_응답_DTO_생성();
        userMissionInfoWithFinishedGetListDto = 유저미션_완료여부_응답_생성();
    }

    @Test
    void 스탬프유저_회원가입_성공() throws Exception {
        given(stampUserService.signup(anyLong(), any(SignupWithoutCustomDto.class)))
                .willReturn(stampUserGetDto);

        mockMvc.perform(post("/stamp/user")
                        .param("stampId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupWithoutCustomDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").value(stampUserGetDto.getUuid()))
                .andExpect(jsonPath("$.participantCount").value(stampUserGetDto.getParticipantCount()))
                .andExpect(jsonPath("$.finished").value(false))
                .andExpect(jsonPath("$.userMissionInfoGetDtos").isArray())
                .andExpect(jsonPath("$.userMissionInfoGetDtos[0].userMissionId").value(1L))
                .andExpect(jsonPath("$.userMissionInfoGetDtos[0].missionId").value(11L))
                .andExpect(jsonPath("$.userMissionInfoGetDtos[0].clear").value(false))
                .andExpect(jsonPath("$.userMissionInfoGetDtos[1].userMissionId").value(2L))
                .andExpect(jsonPath("$.userMissionInfoGetDtos[1].missionId").value(12L))
                .andExpect(jsonPath("$.userMissionInfoGetDtos[1].clear").value(true))
                .andDo(StampUserDocs.signup());
    }

    @Test
    void 스탬프유저_회원가입_stampId_검증실패() throws Exception {
        mockMvc.perform(post("/stamp/user")
                        .param("stampId", "0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupWithoutCustomDto)))
                .andExpect(status().isBadRequest())
                .andDo(StampUserDocs.errorSnippet("스탬프유저_회원가입_stampId_검증실패"));
    }

    @Test
    void 스탬프유저_회원가입_body_검증실패() throws Exception {
        SignupDto invalidDto = 유효하지_않는_회원가입_DTO_생성();
        mockMvc.perform(post("/stamp/user")
                        .param("stampId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andDo(StampUserDocs.errorSnippet("스탬프유저_회원가입_body_검증실패"));
    }

    @Test
    void 스템프유저_커스텀_회원가입_성공() throws Exception {
        given(stampUserService.signup(anyLong(), any(SignupWithCustomDto.class)))
                .willReturn(stampUserGetDto);
        mockMvc.perform(post("/stamp/user/custom")
                        .param("stampId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupWithCustomDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").value(stampUserGetDto.getUuid()))
                .andExpect(jsonPath("$.participantCount").value(stampUserGetDto.getParticipantCount()))
                .andExpect(jsonPath("$.finished").value(false))
                .andExpect(jsonPath("$.userMissionInfoGetDtos").isArray())
                .andExpect(jsonPath("$.userMissionInfoGetDtos[0].userMissionId").value(1L))
                .andExpect(jsonPath("$.userMissionInfoGetDtos[0].missionId").value(11L))
                .andExpect(jsonPath("$.userMissionInfoGetDtos[0].clear").value(false))
                .andExpect(jsonPath("$.userMissionInfoGetDtos[1].userMissionId").value(2L))
                .andExpect(jsonPath("$.userMissionInfoGetDtos[1].missionId").value(12L))
                .andExpect(jsonPath("$.userMissionInfoGetDtos[1].clear").value(true))
                .andDo(StampUserDocs.signupCustom());
    }

    @Test
    void 스탬프유저_커스텀_회원가입_stampId_검증실패() throws Exception {
        mockMvc.perform(post("/stamp/user/custom")
                        .param("stampId", "0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupWithCustomDto)))
                .andExpect(status().isBadRequest())
                .andDo(StampUserDocs.errorSnippet("스탬프유저_커스텀_회원가입_stampId_검증실패"));
    }

    @Test
    void 스탬프유저_커스텀_회원가입_body_검증실패() throws Exception {
        SignupDto invalidDto = 유효하지_않는_커스텀_회원가입_DTO_생성();
        mockMvc.perform(post("/stamp/user/custom")
                        .param("stampId", "0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andDo(StampUserDocs.errorSnippet("스탬프유저_커스텀_회원가입_body_검증실패"));
    }

    @Test
    void 스템프유저_회원가입_v2_성공() throws Exception {
        given(stampUserService.signupV2(anyLong(), any(SignupDto.class))).willReturn(stampUserGetDto);
        mockMvc.perform(post("/stamp/user/signup")
                        .param("stampId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").value(stampUserGetDto.getUuid()))
                .andExpect(jsonPath("$.participantCount").value(stampUserGetDto.getParticipantCount()))
                .andExpect(jsonPath("$.finished").value(false))
                .andExpect(jsonPath("$.userMissionInfoGetDtos").isArray())
                .andExpect(jsonPath("$.userMissionInfoGetDtos[0].userMissionId").value(1L))
                .andExpect(jsonPath("$.userMissionInfoGetDtos[0].missionId").value(11L))
                .andExpect(jsonPath("$.userMissionInfoGetDtos[0].clear").value(false))
                .andExpect(jsonPath("$.userMissionInfoGetDtos[1].userMissionId").value(2L))
                .andExpect(jsonPath("$.userMissionInfoGetDtos[1].missionId").value(12L))
                .andExpect(jsonPath("$.userMissionInfoGetDtos[1].clear").value(true))
                .andDo(StampUserDocs.signupV2());
    }

    @Test
    void 스탬프유저_회원가입_v2_stampId_검증실패() throws Exception {
        mockMvc.perform(post("/stamp/user/signup")
                        .param("stampId", "0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupWithCustomDto)))
                .andExpect(status().isBadRequest())
                .andDo(StampUserDocs.errorSnippet("스탬프유저_회원가입_v2_stampId_검증실패"));
    }

    @Test
    void 스탬프유저_회원가입_v2_body_검증실패() throws Exception {
        SignupDto invalidDto = 유효하지_않는_커스텀_회원가입_DTO_생성();
        mockMvc.perform(post("/stamp/user/signup")
                        .param("stampId", "0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andDo(StampUserDocs.errorSnippet("스탬프유저_회원가입_v2_body_검증실패"));
    }

    @Test
    void 스탬프유저_로그인_성공() throws Exception {
        given(stampUserService.login(anyLong(), any(LoginDto.class))).willReturn(stampUserGetDto);

        // when & then
        mockMvc.perform(post("/stamp/user/login")
                        .param("stampId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").value(stampUserGetDto.getUuid()))
                .andExpect(jsonPath("$.participantCount").value(stampUserGetDto.getParticipantCount()))
                .andExpect(jsonPath("$.finished").value(stampUserGetDto.isFinished()))
                .andExpect(jsonPath("$.userMissionInfoGetDtos").isArray())
                .andExpect(jsonPath("$.userMissionInfoGetDtos[0].userMissionId").value(1L))
                .andExpect(jsonPath("$.userMissionInfoGetDtos[0].missionId").value(11L))
                .andExpect(jsonPath("$.userMissionInfoGetDtos[0].clear").value(false))
                .andExpect(jsonPath("$.userMissionInfoGetDtos[1].userMissionId").value(2L))
                .andExpect(jsonPath("$.userMissionInfoGetDtos[1].missionId").value(12L))
                .andExpect(jsonPath("$.userMissionInfoGetDtos[1].clear").value(true))
                .andDo(StampUserDocs.login());
    }

    @Test
    void 스탬프유저_로그인_stampId_검증실패() throws Exception {
        mockMvc.perform(post("/stamp/user/login")
                        .param("stampId", "0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isBadRequest())
                .andDo(StampUserDocs.errorSnippet("스탬프유저_로그인_stampId_검증실패"));
    }

    @Test
    void 스탬프유저_로그인_name_누락_검증실패() throws Exception {
        LoginDto dto = new LoginDto("", "01012345678");
        mockMvc.perform(post("/stamp/user/login")
                        .param("stampId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andDo(StampUserDocs.errorSnippet("스탬프유저_로그인_name_누락_검증실패"));
    }

    @Test
    @WithMockUser(
            username = "admin",
            roles = {"ADMIN"})
    void 미션_현황_조회_성공() throws Exception {
        given(stampUserService.getUserMissionWithFinished(TEST_UUID)).willReturn(userMissionInfoWithFinishedGetListDto);
        mockMvc.perform(get("/stamp/user/{uuid}", TEST_UUID)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, ADMIN_TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.finished").value(false))
                .andExpect(jsonPath("$.userMissionInfoGetDtos[0].userMissionId").value(1L))
                .andExpect(jsonPath("$.userMissionInfoGetDtos[0].missionId").value(11L))
                .andExpect(jsonPath("$.userMissionInfoGetDtos[0].clear").value(false))
                .andExpect(jsonPath("$.userMissionInfoGetDtos[1].userMissionId").value(2L))
                .andExpect(jsonPath("$.userMissionInfoGetDtos[1].missionId").value(12L))
                .andExpect(jsonPath("$.userMissionInfoGetDtos[1].clear").value(true))
                .andDo(StampUserDocs.getMissionInfo());
    }

    @Test
    void 미션_현황_조회_권한없음() throws Exception {
        mockMvc.perform(get("/stamp/user/{uuid}", TEST_UUID)
                        .param("stampId", "1")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, ADMIN_TOKEN))
                .andExpect(status().isUnauthorized())
                .andDo(StampUserDocs.errorSnippet("미션_현황_조회_권한없음"));
    }

    @Test
    @WithMockUser(
            username = "admin",
            roles = {"ADMIN"})
    void 스탬프_유저_전체_조회_성공() throws Exception {
        List<StampUsersGetDto> responseDtos = 스탬프유저들_응답_DTO_생성();
        given(stampUserService.getStampUsers(anyLong())).willReturn(responseDtos);
        mockMvc.perform(get("/stamp/user/all").param("stampId", "1").header(HttpHeaders.AUTHORIZATION, ADMIN_TOKEN))
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
                .andDo(StampUserDocs.getStampUsers());
    }

    @Test
    @WithMockUser(
            username = "admin",
            roles = {"ADMIN"})
    void 스탬프_유저_전체_조회_stampId_검증실패() throws Exception {
        mockMvc.perform(get("/stamp/user/all")
                        .param("stampId", "0")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, ADMIN_TOKEN))
                .andExpect(status().isBadRequest())
                .andDo(StampUserDocs.errorSnippet("스탬프_유저_전체_조회_stampId_검증실패"));
    }

    @Test
    void 스탬프_유저_전체_조회_권한없음() throws Exception {
        mockMvc.perform(get("/stamp/user/all")
                        .param("stampId", "0")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, ADMIN_TOKEN))
                .andExpect(status().isUnauthorized())
                .andDo(StampUserDocs.errorSnippet("스탬프_유저_전체_조회_권한없음"));
    }

    @Test
    void 유저_미션_업데이트_성공() throws Exception {
        doNothing().when(stampUserService).updateUserMission(anyString(), anyLong());
        mockMvc.perform(patch("/stamp/user/{uuid}/{userMissionId}", TEST_UUID, 1L))
                .andExpect(status().isOk())
                .andDo(StampUserDocs.updateUserMission());
    }

    @Test
    void 유저_미션_업데이트_uuid_유효성검사_실패() throws Exception {
        String invalidUuid = "wrong-uuid";
        mockMvc.perform(patch("/stamp/user/{uuid}/{userMissionId}", invalidUuid, 1L))
                .andExpect(status().isBadRequest())
                .andDo(StampUserDocs.errorSnippet("유저_미션_업데이트_uuid_유효성검사_실패"));
    }

    @Test
    void 유저_미션_업데이트_userMissionId_유효성검사_실패() throws Exception {
        mockMvc.perform(patch("/stamp/user/{uuid}/{userMissionId}", TEST_UUID, 0))
                .andExpect(status().isBadRequest())
                .andDo(StampUserDocs.errorSnippet("유저_미션_업데이트_userMissionId_유효성검사_실패"));
    }

    @Test
    void 전체_미션_완료여부_갱신_완료_성공() throws Exception {
        given(stampUserService.checkFinish(anyString())).willReturn("스탬프 투어 완료");

        mockMvc.perform(patch("/stamp/user/check/{uuid}", TEST_UUID))
                .andExpect(status().isOk())
                .andExpect(content().string("스탬프 투어 완료"))
                .andDo(StampUserDocs.checkFinish());
    }

    @Test
    void 전체_미션_완료여부_갱신_미완료_성공() throws Exception {
        given(stampUserService.checkFinish(anyString())).willReturn("미완료");

        mockMvc.perform(patch("/stamp/user/check/{uuid}", TEST_UUID))
                .andExpect(status().isOk())
                .andExpect(content().string("미완료"))
                .andDo(StampUserDocs.checkFinish());
    }

    @Test
    void 전체_미션_완료여부_갱신_uuid길이_실패() throws Exception {
        String invalidUuid = "wrong-uuid";
        mockMvc.perform(patch("/stamp/user/check/{uuid}", invalidUuid))
                .andExpect(status().isBadRequest())
                .andDo(StampUserDocs.errorSnippet("전체_미션_완료여부_갱신_uuid길이_실패"));
    }

    @Test
    void 전체_미션_완료여부_갱신_v2_미완료_성공() throws Exception {
        given(stampUserService.checkV2Finish(anyString())).willReturn("미완료");
        mockMvc.perform(patch("/stamp/user/check/v2/{uuid}", TEST_UUID))
                .andExpect(status().isOk())
                .andExpect(content().string("미완료"))
                .andDo(StampUserDocs.checkFinishV2());
    }

    @Test
    void 전체_미션_완료여부_갱신_v2_완료_성공() throws Exception {
        given(stampUserService.checkV2Finish(anyString())).willReturn("스탬프 투어 완료");
        mockMvc.perform(patch("/stamp/user/check/v2/{uuid}", TEST_UUID))
                .andExpect(status().isOk())
                .andExpect(content().string("스탬프 투어 완료"))
                .andDo(StampUserDocs.checkFinishV2());
    }

    @Test
    void 전체_미션_완료여부_갱신_v2_uuid길이_실패() throws Exception {
        String invalidUuid = "wrong-uuid";
        mockMvc.perform(patch("/stamp/user/check/v2/{uuid}", invalidUuid))
                .andExpect(status().isBadRequest())
                .andDo(StampUserDocs.errorSnippet("전체_미션_완료여부_갱신_v2_uuid길이_실패"));
    }

    @Test
    @WithMockUser(
            username = "admin",
            roles = {"ADMIN"})
    void 스탬프유저_삭제_성공() throws Exception {
        doNothing().when(stampUserService).deleteStampByUuid(anyString());
        mockMvc.perform(delete("/stamp/user/{uuid}", TEST_UUID).header(HttpHeaders.AUTHORIZATION, ADMIN_TOKEN))
                .andExpect(status().isOk())
                .andDo(StampUserDocs.deleteStampUser());
    }

    @Test
    @WithMockUser(
            username = "admin",
            roles = {"ADMIN"})
    void 스탬프유저_삭제_uuid_길이_실패() throws Exception {
        String invalidUuid = "wrong-uuid";
        mockMvc.perform(delete("/stamp/user/{uuid}", invalidUuid).header(HttpHeaders.AUTHORIZATION, ADMIN_TOKEN))
                .andExpect(status().isBadRequest())
                .andDo(StampUserDocs.errorSnippet("스탬프유저_삭제_실패_uuid_길이_실패"));
    }

    @Test
    void 스탬프유저_삭제_권한없음_실패() throws Exception {
        mockMvc.perform(delete("/stamp/user/{uuid}", TEST_UUID).header(HttpHeaders.AUTHORIZATION, ADMIN_TOKEN))
                .andExpect(status().isUnauthorized())
                .andDo(StampUserDocs.errorSnippet("스탬프유저_삭제_권한없음_실패"));
    }
}
