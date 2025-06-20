package com.halo.eventer.domain.stamp.service;

import java.util.List;
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
import com.halo.eventer.domain.festival.exception.FestivalNotFoundException;
import com.halo.eventer.domain.festival.repository.FestivalRepository;
import com.halo.eventer.domain.stamp.Mission;
import com.halo.eventer.domain.stamp.Stamp;
import com.halo.eventer.domain.stamp.StampUser;
import com.halo.eventer.domain.stamp.dto.stamp.*;
import com.halo.eventer.domain.stamp.exception.StampClosedException;
import com.halo.eventer.domain.stamp.exception.StampNotFoundException;
import com.halo.eventer.domain.stamp.repository.MissionRepository;
import com.halo.eventer.domain.stamp.repository.StampRepository;
import com.halo.eventer.global.utils.EncryptService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@SuppressWarnings("NonAsciiCharacters")
public class StampServiceTest {

    @Mock
    private StampRepository stampRepository;

    @Mock
    private FestivalRepository festivalRepository;

    @Mock
    private EncryptService encryptService;

    @Mock
    private MissionRepository missionRepository;

    @InjectMocks
    private StampService stampService;

    private Festival festival;
    private FestivalCreateDto festivalCreateDto;
    private Stamp stamp;
    private MissionSetListDto missionSetListDto;
    private List<MissionSetDto> missionSetDtos;

    @BeforeEach
    void setUp() {
        festivalCreateDto = new FestivalCreateDto("test festival", "test");
        festival = Festival.from(festivalCreateDto);
        stamp = Stamp.create(festival);
        missionSetListDto = new MissionSetListDto();
        missionSetDtos = setupMissionSetDtos();
        setField(missionSetListDto, "missionSets", missionSetDtos);
    }

    @Test
    void 축제id로_스탬프_생성_성공() {
        // given
        given(festivalRepository.findById(anyLong())).willReturn(Optional.ofNullable(festival));
        given(stampRepository.save(any(Stamp.class))).willReturn(stamp);
        List<Stamp> stamps = List.of(stamp);
        given(stampRepository.findByFestival(festival)).willReturn(stamps);

        // when
        List<StampGetDto> result = stampService.registerStamp(1L);

        // then
        assertThat(result).hasSize(1);
    }

    @Test
    void 축제id로_스탬프_생성_실패() {
        // given
        long notExistFestivalId = 999L;
        given(festivalRepository.findById(notExistFestivalId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> stampService.registerStamp(notExistFestivalId))
                .isInstanceOf(FestivalNotFoundException.class);
    }

    @Test
    void 축제_id로_스탬프_조회_성공() {
        given(festivalRepository.findById(anyLong())).willReturn(Optional.ofNullable(festival));
        List<Stamp> stamps = List.of(Stamp.create(festival));
        given(stampRepository.findByFestival(festival)).willReturn(stamps);

        // when
        List<StampGetDto> result = stampService.getStampByFestivalId(1L);

        // then
        assertThat(result).hasSize(1);
    }

    @Test
    void 축제_id로_스탬프_조회_실패() {
        // given
        given(festivalRepository.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> stampService.getStampByFestivalId(anyLong()))
                .isInstanceOf(FestivalNotFoundException.class);
    }

    @Test
    void 스탬프_상태_변경_성공() {
        // given
        stamp.switchActivation();
        given(stampRepository.findById(anyLong())).willReturn(Optional.ofNullable(stamp));

        // when
        stampService.updateStampOn(1L);

        // then
        assertThat(stamp.isActive()).isTrue();
    }

    @Test
    void 스탬프_상태_변경_실패() {
        // given
        given(stampRepository.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> stampService.updateStampOn(anyLong())).isInstanceOf(StampNotFoundException.class);
    }

    @Test
    void 스탬프_삭제_성공() {
        // given
        given(stampRepository.findById(anyLong())).willReturn(Optional.of(stamp));

        // when
        stampService.deleteStamp(1L);

        // then
        verify(stampRepository, times(1)).delete(stamp);
    }

    @Test
    void 미션_생성_성공() {
        // given
        given(stampRepository.findById(anyLong())).willReturn(Optional.of(stamp));

        // when
        stampService.createMission(1L, missionSetListDto);

        // then
        verify(missionRepository, times(1)).saveAll(anyList());
    }

    @Test
    void 미션_생성_실패() {
        // given
        stamp.switchActivation();
        given(stampRepository.findById(anyLong())).willReturn(Optional.of(stamp));

        // when & then
        assertThatThrownBy(() -> stampService.createMission(anyLong(), missionSetListDto))
                .isInstanceOf(StampClosedException.class);
    }

    @Test
    void 미션_리스트_조회_성공() {
        // given
        Mission mission1 = setUpMission(1L, "미션1");
        Mission mission2 = setUpMission(2L, "미션2");
        mission1.addStamp(stamp);
        mission2.addStamp(stamp);
        given(stampRepository.findById(anyLong())).willReturn(Optional.of(stamp));

        // when
        List<MissionSummaryGetDto> result = stampService.getMissions(1L);

        // then
        assertThat(result).hasSize(2);
        assertThat(result).extracting("title").containsExactly("미션1", "미션2");
    }

    @Test
    void 미션_리스트_조회_실패() {
        // given
        given(stampRepository.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> stampService.getMissions(anyLong())).isInstanceOf(StampNotFoundException.class);
    }

    @Test
    void 해당_스탬프_유저들_조회_성공() {
        // given
        StampUser stampUser1 = new StampUser(stamp, "암호화번호1", "암호화이름1", 1);
        stamp.getStampUsers().add(stampUser1);
        given(stampRepository.findById(anyLong())).willReturn(Optional.ofNullable(stamp));
        given(encryptService.decryptInfo(anyString())).willReturn("암호화");

        // when
        List<StampUsersGetDto> result = stampService.getStampUsers(1L);

        // then
        assertThat(result).hasSize(1);
        assertThat(result).extracting("name").containsExactly("암호화");
    }

    @Test
    void 미션성공_기준_정하기_성공() {
        // given
        Integer finishCnt = 5;
        given(stampRepository.findById(1L)).willReturn(Optional.of(stamp));

        // when
        stampService.setFinishCnt(1L, finishCnt);

        // then
        assertThat(stamp.getFinishCount()).isEqualTo(finishCnt);
    }

    @Test
    void 미션성공_기준_정하기_실패() {
        // given
        given(stampRepository.findById(1L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> stampService.setFinishCnt(1L, 3)).isInstanceOf(StampNotFoundException.class);
    }

    private Mission setUpMission(Long id, String missionTitle) {
        Mission mission = new Mission();
        setField(mission, "id", id);
        setField(mission, "title", missionTitle);
        return mission;
    }

    private List<MissionSetDto> setupMissionSetDtos() {
        return List.of(
                new MissionSetDto(1L, "부스 A", "미션 내용 A", "장소 A", "오전 10시", "clearedA.png", "notClearedA.png"),
                new MissionSetDto(2L, "부스 B", "미션 내용 B", "장소 B", "오전 11시", "clearedB.png", "notClearedB.png"));
    }
}
