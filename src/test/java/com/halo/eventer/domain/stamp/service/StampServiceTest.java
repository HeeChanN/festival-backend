package com.halo.eventer.domain.stamp.service;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.exception.FestivalNotFoundException;
import com.halo.eventer.domain.festival.repository.FestivalRepository;
import com.halo.eventer.domain.stamp.Mission;
import com.halo.eventer.domain.stamp.Stamp;
import com.halo.eventer.domain.stamp.StampUser;
import com.halo.eventer.domain.stamp.dto.mission.request.MissionSetReqDto;
import com.halo.eventer.domain.stamp.dto.mission.response.MissionSummaryResDto;
import com.halo.eventer.domain.stamp.dto.stamp.*;
import com.halo.eventer.domain.stamp.exception.StampClosedException;
import com.halo.eventer.domain.stamp.exception.StampNotFoundException;
import com.halo.eventer.domain.stamp.repository.MissionRepository;
import com.halo.eventer.domain.stamp.repository.StampRepository;
import com.halo.eventer.global.utils.EncryptService;

import static com.halo.eventer.domain.festival.FestivalFixture.축제_엔티티;
import static com.halo.eventer.domain.stamp.fixture.MissionFixture.*;
import static com.halo.eventer.domain.stamp.fixture.StampFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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
    private Stamp stamp1;
    private List<Stamp> stamps;
    private List<MissionSetReqDto> missionSetDtos;

    @BeforeEach
    void setUp() {
        festival = 축제_엔티티();
        stamp1 = 스탬프1_생성(festival);
        stamps = 모든_스탬프(festival);
        festival = stamp1.getFestival();
        missionSetDtos = 미션_셋업_리스트();
    }

    @Test
    void 축제id로_스탬프_생성_성공() {
        // given
        given(festivalRepository.findById(anyLong())).willReturn(Optional.ofNullable(festival));
        given(stampRepository.save(any(Stamp.class))).willReturn(stamp1);
        given(stampRepository.findByFestival(festival)).willReturn(List.of(stamp1));

        // when
        List<StampGetDto> results = stampService.registerStamp(1L);
        StampGetDto result = results.get(0);

        // then
        assertThat(results).hasSize(1);
        assertThat(result.isStampOn()).isEqualTo(stamp1.getActive());
        assertThat(result.getStampId()).isEqualTo(stamp1.getId());
        assertThat(result.getStampFinishCnt()).isEqualTo(stamp1.getFinishCount());
    }

    @Test
    void 축제id로_스탬프_생성_다중스탬프_조회_성공() {
        // given
        given(festivalRepository.findById(anyLong())).willReturn(Optional.ofNullable(festival));
        given(stampRepository.save(any(Stamp.class))).willReturn(stamp1);
        given(stampRepository.findByFestival(festival)).willReturn(stamps);

        // when
        List<StampGetDto> results = stampService.registerStamp(1L);

        // then
        assertThat(results).hasSize(3);
        assertThat(results.get(0).isStampOn()).isEqualTo(stamp1.getActive());
        assertThat(results.get(0).getStampId()).isEqualTo(stamp1.getId());
        assertThat(results.get(0).getStampFinishCnt()).isEqualTo(stamp1.getFinishCount());
    }

    @Test
    void 스탬프_생성_존재하지_않는_축제_ID_실패() {
        // given
        long notExistFestivalId = 999L;
        given(festivalRepository.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> stampService.registerStamp(notExistFestivalId))
                .isInstanceOf(FestivalNotFoundException.class);
    }

    @Test
    void 축제_id로_스탬프_조회_성공() {
        // given
        given(festivalRepository.findById(anyLong())).willReturn(Optional.ofNullable(festival));
        given(stampRepository.findByFestival(any(Festival.class))).willReturn(stamps);

        // when
        List<StampGetDto> results = stampService.getStampByFestivalId(1L);
        StampGetDto result = results.get(0);

        // then
        assertThat(results).hasSize(3);
        assertThat(result.isStampOn()).isEqualTo(stamp1.getActive());
        assertThat(result.getStampId()).isEqualTo(stamp1.getId());
        assertThat(result.getStampFinishCnt()).isEqualTo(stamp1.getFinishCount());
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
        given(stampRepository.findById(anyLong())).willReturn(Optional.ofNullable(stamp1));
        boolean before = stamp1.getActive();

        // when
        stampService.updateStampOn(스탬프1_ID);
        boolean after = stamp1.getActive();

        // then
        assertThat(after).isEqualTo(!before);
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
        given(stampRepository.findById(anyLong())).willReturn(Optional.of(stamp1));

        // when
        stampService.deleteStamp(스탬프1_ID);

        // then
        verify(stampRepository, times(1)).delete(stamp1);
    }

    @Test
    void 스탬프_삭제_실패_스탬프없음() {
        // given
        given(stampRepository.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> stampService.deleteStamp(스탬프1_ID)).isInstanceOf(StampNotFoundException.class);
    }

    // TODO : MissionService로 옮길 예정
    @Test
    void 미션_생성_성공() {
        // given
        given(stampRepository.findById(anyLong())).willReturn(Optional.of(stamp1));

        // when
        stampService.createMission(1L, missionSetDtos);

        // then
        verify(missionRepository, times(1)).saveAll(anyList());
        assertThat(stamp1.getMissions()).hasSize(5);
        //        assertThat(stamp.getMissions())
        //                .extracting("title")
        //                .containsExactly("부스 A", "부스 B");
    }

    @Test
    // TODO : MissionService로 옮길 예정
    void 미션_생성_스탬프_비활성화_실패() {
        // given
        stamp1.switchActivation();
        given(stampRepository.findById(anyLong())).willReturn(Optional.of(stamp1));

        // when & then
        assertThatThrownBy(() -> stampService.createMission(스탬프1_ID, missionSetDtos))
                .isInstanceOf(StampClosedException.class);
    }

    @Test
    // TODO : MissionService로 옮길 예정
    void 미션_리스트_조회_성공() {
        // given
        Mission mission1 = 미션1_생성();
        Mission mission2 = 미션2_생성();
        mission1.addStamp(stamp1);
        mission2.addStamp(stamp1);
        given(stampRepository.findById(anyLong())).willReturn(Optional.of(stamp1));

        // when
        List<MissionSummaryResDto> result = stampService.getMissions(1L);

        // then
        assertThat(result).hasSize(2);
        assertThat(result).extracting("title").containsExactly("미션 1", "미션 2");
    }

    @Test
    // TODO : MissionService로 옮길 예정
    void 미션_리스트_조회_실패() {
        // given
        given(stampRepository.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> stampService.getMissions(anyLong())).isInstanceOf(StampNotFoundException.class);
    }

    @Test
    // TODO : StampUserService로 이동 예정
    void 해당_스탬프_유저들_조회_성공() {
        // given
        StampUser stampUser1 = new StampUser("암호화번호1", "암호화이름1", 1);
        stampUser1.addStamp(stamp1);
        given(stampRepository.findById(anyLong())).willReturn(Optional.ofNullable(stamp1));
        given(encryptService.decryptInfo(anyString())).willReturn("암호화");

        // when
        List<StampUsersGetDto> result = stampService.getStampUsers(스탬프1_ID);

        // then
        assertThat(result).hasSize(1);
        assertThat(result).extracting("name").containsExactly("암호화");
    }

    @Test
    void 미션성공_기준_정하기_성공() {
        // given
        Integer 미션성공_기준 = 5;
        given(stampRepository.findById(anyLong())).willReturn(Optional.of(stamp1));

        // when
        stampService.setFinishCnt(스탬프1_ID, 미션성공_기준);

        // then
        assertThat(stamp1.getFinishCount()).isEqualTo(미션성공_기준);
    }

    @Test
    void 미션성공_기준_정하기_실패() {
        // given
        given(stampRepository.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> stampService.setFinishCnt(스탬프1_ID, anyInt()))
                .isInstanceOf(StampNotFoundException.class);
    }
}
