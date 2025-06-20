package com.halo.eventer.domain.stamp.service;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import com.halo.eventer.domain.stamp.Mission;
import com.halo.eventer.domain.stamp.Stamp;
import com.halo.eventer.domain.stamp.StampUser;
import com.halo.eventer.domain.stamp.UserMission;
import com.halo.eventer.domain.stamp.dto.stampUser.*;
import com.halo.eventer.domain.stamp.exception.StampClosedException;
import com.halo.eventer.domain.stamp.exception.StampUserAlreadyExistsException;
import com.halo.eventer.domain.stamp.exception.StampUserNotFoundException;
import com.halo.eventer.domain.stamp.exception.UserMissionNotFoundException;
import com.halo.eventer.domain.stamp.fixture.MissionFixture;
import com.halo.eventer.domain.stamp.fixture.StampFixture;
import com.halo.eventer.domain.stamp.fixture.StampUserFixture;
import com.halo.eventer.domain.stamp.fixture.UserMissionFixture;
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

    // request dto
    private SignupDto signupDto;
    private SignupWithoutCustomDto signupWithoutCustomDto;
    private SignupWithCustomDto signupWithCustomDto;
    private LoginDto loginDto;

    private static final String ENCRYPTED_STRING = "encrypted string";

    @BeforeEach
    public void setUp() {
        // entity setup
        stamp = StampFixture.스탬프_엔티티_생성();
        stampUser = StampUserFixture.스탬프유저_엔티티_생성(stamp);
        mission = MissionFixture.미션_엔티티_생성();
        userMission1 = UserMissionFixture.유저미션_엔티티_생성(1L, stampUser, mission);

        // dto setup
        signupDto = StampUserFixture.회원가입_DTO_생성();
        signupWithoutCustomDto = StampUserFixture.커스텀_없는_회원가입_DTO_생성();
        signupWithCustomDto = StampUserFixture.커스텀_있는_회원가입_DTO_생성();
        loginDto = StampUserFixture.로그인_DTO_생성();
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
    void 스탬프_유저_생성_학교번호_있음() {
        // given
        mission.addStamp(stamp);
        given(stampRepository.findById(anyLong())).willReturn(Optional.of(stamp));
        given(encryptService.encryptInfo(anyString())).willReturn(ENCRYPTED_STRING);
        given(stampUserRepository.existsByStampIdAndPhone(anyLong(), anyString()))
                .willReturn(false);
        setField(signupDto, "schoolNo", "test school no");
        // when
        StampUserGetDto result = stampUserService.signup(1L, signupDto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.isFinished()).isEqualTo(false);
        assertThat(result.getUserMissionInfoGetDtos()).hasSize(1);
    }

    @Test
    void 스탬프_유저_생성_학교번호_없음() {
        // given
        mission.addStamp(stamp);
        given(stampRepository.findById(anyLong())).willReturn(Optional.of(stamp));
        given(encryptService.encryptInfo(anyString())).willReturn(ENCRYPTED_STRING);
        given(stampUserRepository.existsByStampIdAndPhone(anyLong(), anyString()))
                .willReturn(false);
        // when
        StampUserGetDto result = stampUserService.signup(1L, signupDto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.isFinished()).isEqualTo(false);
        assertThat(result.getUserMissionInfoGetDtos()).hasSize(1);
    }

    @Test
    void 스탬프_유저_생성_스탬프_off_실패() {
        // given
        given(stampRepository.findById(anyLong())).willReturn(Optional.of(stamp));
        stamp.switchActivation();

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
        stampUser.getUserMissions().forEach(UserMission::markAsComplete);
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
        stamp.defineFinishCnt(3);

        // when
        String result = stampUserService.checkV2Finish(anyString());
        System.out.println(stamp.getFinishCount());

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

    private UserMission setupUserMission(Long id, StampUser stampUser, Mission mission) {
        UserMission userMission = UserMission.create(mission, stampUser);
        setField(userMission, "id", id);
        return userMission;
    }
}
