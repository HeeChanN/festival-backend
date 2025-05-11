package com.halo.eventer.domain.stamp.service;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.dto.FestivalCreateDto;
import com.halo.eventer.domain.stamp.Mission;
import com.halo.eventer.domain.stamp.Stamp;
import com.halo.eventer.domain.stamp.StampUser;
import com.halo.eventer.domain.stamp.UserMission;
import com.halo.eventer.domain.stamp.dto.stampUser.*;
import com.halo.eventer.domain.stamp.exception.StampClosedException;
import com.halo.eventer.domain.stamp.exception.StampUserAlreadyExistsException;
import com.halo.eventer.domain.stamp.exception.StampUserNotFoundException;
import com.halo.eventer.domain.stamp.exception.UserMissionNotFoundException;
import com.halo.eventer.domain.stamp.repository.StampRepository;
import com.halo.eventer.domain.stamp.repository.StampUserRepository;
import com.halo.eventer.global.utils.EncryptService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@SuppressWarnings("NonAsciiCharacters")
public class StampUserServiceTest {

    @Mock
    private StampUserRepository stampUserRepository;

    @Mock
    private EncryptService encryptService;

    @Mock
    private StampRepository stampRepository;

    @InjectMocks
    private StampUserService stampUserService;

    // entity
    private Stamp stamp;
    private StampUser stampUser;
    private Mission mission;
    private UserMission userMission1;
    private UserMission userMission2;

    // request dto
    private SignupWithoutCustomDto signupWithoutCustomDto;
    private SignupWithCustomDto signupWithCustomDto;
    private LoginDto loginDto;

    private static final String ENCRYPTED_STRING = "encrypted string";

    @BeforeEach
    public void setUp() {
        // entity setup
        stamp = setUpStamp();
        stampUser = setUpStampUser();
        mission = setUpMission();
        userMission1 = setupUserMission(1L, stampUser, mission);
        userMission2 = setupUserMission(2L, stampUser, mission);
        // dto setup
        signupWithoutCustomDto = setUpSignupWithoutCustomDto();
        signupWithCustomDto = setUpsignupWithCustomDto();
        loginDto = setUpLoginDto();
    }

    @Test
    void 커스텀x_스탬프_유저_생성_성공() {
        // given
        mission.addStamp(stamp);
        given(stampRepository.findById(anyLong())).willReturn(Optional.of(stamp));
        given(encryptService.encryptInfo(anyString())).willReturn(ENCRYPTED_STRING);
        given(stampUserRepository.existsByStampIdAndPhone(anyLong(), anyString()))
                .willReturn(false);
        given(stampUserRepository.save(any(StampUser.class))).willReturn(stampUser);
        // when

        StampUserGetDto result = stampUserService.signup(1L, signupWithoutCustomDto);
        // then
        assertThat(result).isNotNull();
        assertThat(result.isFinished()).isEqualTo(false);
        assertThat(result.getUserMissionInfoGetDtos()).hasSize(1);
    }

    @Test
    void 커스텀_스탬프_유저_생성_성공() {
        // given
        mission.addStamp(stamp);
        given(stampRepository.findById(anyLong())).willReturn(Optional.of(stamp));
        given(encryptService.encryptInfo(anyString())).willReturn(ENCRYPTED_STRING);
        given(stampUserRepository.existsByStampIdAndPhone(anyLong(), anyString()))
                .willReturn(false);
        // when

        StampUserGetDto result = stampUserService.signup(1L, signupWithCustomDto);
        // then
        assertThat(result).isNotNull();
        assertThat(result.isFinished()).isEqualTo(false);
        assertThat(result.getUserMissionInfoGetDtos()).hasSize(1);
    }

    @Test
    void 스탬프_유저_생성_스탬프_off_실패() {
        // given
        given(stampRepository.findById(anyLong())).willReturn(Optional.of(stamp));
        stamp.switchStampOn();

        // when & then
        assertThatThrownBy(() -> stampUserService.signup(1L, signupWithCustomDto))
                .isInstanceOf(StampClosedException.class);
    }

    @Test
    void 스탬프_유저_생성_존재_실패() {
        // given
        given(stampRepository.findById(anyLong())).willReturn(Optional.of(stamp));
        given(encryptService.encryptInfo(anyString())).willReturn(ENCRYPTED_STRING);
        given(stampUserRepository.existsByStampIdAndPhone(anyLong(), anyString()))
                .willReturn(true);

        // when & then
        assertThatThrownBy(() -> stampUserService.signup(1L, signupWithCustomDto))
                .isInstanceOf(StampUserAlreadyExistsException.class);
    }

    @Test
    void 로그인_성공() {
        // given
        given(stampUserRepository.findByStampIdAndPhoneAndName(anyLong(), anyString(), anyString()))
                .willReturn(Optional.of(stampUser));
        given(encryptService.encryptInfo(anyString())).willReturn(ENCRYPTED_STRING);

        // when
        StampUserGetDto result = stampUserService.login(1L, loginDto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getParticipantCount()).isEqualTo(stampUser.getParticipantCount());
    }

    @Test
    void 로그인_실패() {
        // given
        given(encryptService.encryptInfo(anyString())).willReturn(ENCRYPTED_STRING);
        given(stampUserRepository.findByStampIdAndPhoneAndName(anyLong(), anyString(), anyString()))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> stampUserService.login(1L, loginDto)).isInstanceOf(StampUserNotFoundException.class);
    }

    @Test
    void 유저_미션_전체_조회_성공() {
        // given
        given(stampUserRepository.findByUuid(anyString())).willReturn(Optional.of(stampUser));

        // when
        UserMissionInfoWithFinishedGetListDto result = stampUserService.getUserMissionWithFinished(anyString());

        // then
        assertThat(result).isNotNull();
        assertThat(result.isFinished()).isEqualTo(stampUser.isFinished());
    }

    @Test
    void 유저_미션_전체_조회_실패() {
        // given
        given(stampUserRepository.findByUuid(anyString())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> stampUserService.getUserMissionWithFinished(anyString()))
                .isInstanceOf(StampUserNotFoundException.class);
    }

    @Test
    void 사용자_미션_상태_업데이트_성공() {
        // given
        given(stampUserRepository.findByUuid(anyString())).willReturn(Optional.of(stampUser));

        // when
        stampUserService.updateUserMission("", 1L);

        // then
        assertThat(userMission1.isComplete()).isEqualTo(true);
    }

    @Test
    void 사용자_미션_상태_업데이트_실패() {
        // given
        StampUser emptyMissionStamp = new StampUser();
        given(stampUserRepository.findByUuid(anyString())).willReturn(Optional.of(emptyMissionStamp));

        // when & then
        assertThatThrownBy(() -> stampUserService.updateUserMission("", 1L))
                .isInstanceOf(UserMissionNotFoundException.class);
    }

    @Test
    void 사용자_미션_완료_상태_확인_미완료_성공() {
        // given
        given(stampUserRepository.findByUuid(anyString())).willReturn(Optional.of(stampUser));

        // when
        String result = stampUserService.checkFinish(anyString());

        // then
        assertThat(result).isEqualTo("미완료");
    }

    @Test
    void 사용자_미션_완료_상태_확인_완료_성공() {
        // given
        stampUser.getUserMissions().forEach(mission -> mission.updateComplete(true));
        given(stampUserRepository.findByUuid(anyString())).willReturn(Optional.of(stampUser));

        // when
        String result = stampUserService.checkFinish(anyString());

        // then
        assertThat(result).isEqualTo("스탬프 투어 완료");
    }

    @Test
    void v2_사용자_미션_완료_상태_확인_미완료_성공() {
        // given
        given(stampUserRepository.findByUuid(anyString())).willReturn(Optional.of(stampUser));
        stamp.setStampFinishCnt(3);

        // when
        String result = stampUserService.checkV2Finish(anyString());

        // then
        assertThat(result).isEqualTo("미완료");
    }

    @Test
    void v2_사용자_미션_완료_상태_확인_완료_성공() {
        // given
        given(stampUserRepository.findByUuid(anyString())).willReturn(Optional.of(stampUser));

        // when
        String result = stampUserService.checkV2Finish(anyString());

        // then
        assertThat(result).isEqualTo("스탬프 투어 완료");
    }

    @Test
    void 사용자_삭제_성공() {
        // given
        given(stampUserRepository.findByUuid(anyString())).willReturn(Optional.of(stampUser));

        // when
        stampUserService.deleteStampByUuid(anyString());

        // then
        verify(stampUserRepository, times(1)).delete(stampUser);
    }

    @Test
    void 사용자_삭제_실패() {
        // given
        given(stampUserRepository.findByUuid(anyString())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> stampUserService.deleteStampByUuid(anyString()))
                .isInstanceOf(StampUserNotFoundException.class);
    }

    private SignupWithoutCustomDto setUpSignupWithoutCustomDto() {
        SignupWithoutCustomDto signupDto = new SignupWithoutCustomDto();
        setField(signupDto, "name", "test name");
        setField(signupDto, "phone", "test phone");
        setField(signupDto, "participantCount", 10);
        return signupDto;
    }

    private SignupWithCustomDto setUpsignupWithCustomDto() {
        SignupWithCustomDto signupDto = new SignupWithCustomDto();
        setField(signupDto, "name", "test name");
        setField(signupDto, "phone", "test phone");
        setField(signupDto, "participantCount", 10);
        setField(signupDto, "schoolNo", "test school no");
        return signupDto;
    }

    private LoginDto setUpLoginDto() {
        LoginDto loginDto = new LoginDto();
        setField(loginDto, "name", "test");
        setField(loginDto, "phone", "01012341234");
        return loginDto;
    }

    private Mission setUpMission() {
        Mission mission = new Mission();
        setField(mission, "id", 1L);
        setField(mission, "boothId", 1L);
        setField(mission, "title", "mission title");
        setField(mission, "content", "mission content");
        setField(mission, "place", "mission place");
        setField(mission, "time", "mission time");
        setField(mission, "clearedThumbnail", "mission cleared thumbnail");
        setField(mission, "notClearedThumbnail", "mission not cleared thumbnail");
        return mission;
    }

    private StampUser setUpStampUser() {
        return new StampUser(stamp, ENCRYPTED_STRING, ENCRYPTED_STRING, 10);
    }

    private Stamp setUpStamp() {
        Stamp stamp = Stamp.create(Festival.from(new FestivalCreateDto("test festival", "test address")));
        setField(stamp, "id", 1L);
        return stamp;
    }

    private UserMission setupUserMission(Long id, StampUser stampUser, Mission mission) {
        UserMission userMission = new UserMission();
        setField(userMission, "id", id);
        userMission.setMission(mission);
        userMission.setStampUser(stampUser);
        return userMission;
    }
}
