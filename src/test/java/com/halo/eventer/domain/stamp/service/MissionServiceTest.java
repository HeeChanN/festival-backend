package com.halo.eventer.domain.stamp.service;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.halo.eventer.domain.stamp.Mission;
import com.halo.eventer.domain.stamp.Stamp;
import com.halo.eventer.domain.stamp.dto.mission.MissionDetailGetDto;
import com.halo.eventer.domain.stamp.dto.mission.MissionUpdateDto;
import com.halo.eventer.domain.stamp.dto.stamp.MissionSetDto;
import com.halo.eventer.domain.stamp.dto.stamp.MissionSummaryGetDto;
import com.halo.eventer.domain.stamp.exception.MissionNotFoundException;
import com.halo.eventer.domain.stamp.exception.StampClosedException;
import com.halo.eventer.domain.stamp.exception.StampNotFoundException;
import com.halo.eventer.domain.stamp.fixture.MissionFixture;
import com.halo.eventer.domain.stamp.repository.MissionRepository;
import com.halo.eventer.domain.stamp.repository.StampRepository;

import static com.halo.eventer.domain.stamp.fixture.MissionFixture.*;
import static com.halo.eventer.domain.stamp.fixture.StampFixture.스탬프1;
import static com.halo.eventer.domain.stamp.fixture.StampFixture.스탬프1_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
public class MissionServiceTest {

    @Mock
    private MissionRepository missionRepository;

    @Mock
    private StampRepository stampRepository;

    @InjectMocks
    private MissionService missionService;

    private Stamp stamp1;
    private Mission mission;
    private List<MissionSetDto> missionSetDtos;

    @BeforeEach
    void setUp() {
        stamp1 = 스탬프1_생성();
        mission = 미션_엔티티_생성();
        missionSetDtos = 미션_셋업_리스트();
    }

    @Test
    void 미션_생성_성공() {
        // given
        given(stampRepository.findById(anyLong())).willReturn(Optional.of(stamp1));
        List<Mission> expectedMissions =
                missionSetDtos.stream().map(Mission::from).toList();

        // when
        missionService.createMission(1L, missionSetDtos);

        // then
        verify(missionRepository, times(1)).saveAll(anyList());
        assertThat(stamp1.getMissions()).hasSize(5);
        assertThat(stamp1.getMissions())
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id", "stamp")
                .isEqualTo(expectedMissions);
    }

    @Test
    void 미션_생성_스탬프_없음_실패() {
        // given
        given(stampRepository.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> missionService.createMission(스탬프1, missionSetDtos))
                .isInstanceOf(StampNotFoundException.class);
    }

    @Test
    void 미션_생성_스탬프_비활성화_실패() {
        // given
        stamp1.switchActivation();
        given(stampRepository.findById(anyLong())).willReturn(Optional.of(stamp1));

        // when & then
        assertThatThrownBy(() -> missionService.createMission(스탬프1, missionSetDtos))
                .isInstanceOf(StampClosedException.class);
    }

    @Test
    void 미션조회_성공() {
        // given
        given(missionRepository.findById(anyLong())).willReturn(Optional.of(mission));

        // when
        MissionDetailGetDto result = missionService.getMission(1L);

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(MissionDetailGetDto.from(mission));
    }

    @Test
    void 미션조회_실패() {
        // given
        given(missionRepository.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> missionService.getMission(1L)).isInstanceOf(MissionNotFoundException.class);
    }

    @Test
    void 미션_리스트_조회_성공() {
        // given
        Mission mission1 = 미션1_생성();
        Mission mission2 = 미션2_생성();
        mission1.addStamp(stamp1);
        mission2.addStamp(stamp1);
        given(stampRepository.findById(anyLong())).willReturn(Optional.of(stamp1));

        // when
        List<MissionSummaryGetDto> result = missionService.getMissions(1L);

        // then
        assertThat(result).hasSize(2);
        assertThat(result).extracting("title").containsExactly("미션 1", "미션 2");
    }

    @Test
    void 미션_리스트_조회_실패() {
        // given
        given(stampRepository.findById(anyLong())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> missionService.getMissions(anyLong())).isInstanceOf(StampNotFoundException.class);
    }

    @Test
    void 미션수정_성공() {
        // given
        MissionUpdateDto dto = MissionFixture.미션_업데이트_DTO_생성();
        given(missionRepository.findById(anyLong())).willReturn(Optional.of(mission));

        // when
        missionService.updateMission(1L, dto);

        // then
        assertThat(mission.getBoothId()).isEqualTo(dto.getBoothId());
        assertThat(mission.getTitle()).isEqualTo(dto.getTitle());
        assertThat(mission.getContent()).isEqualTo(dto.getContent());
        assertThat(mission.getPlace()).isEqualTo(dto.getPlace());
        assertThat(mission.getTime()).isEqualTo(dto.getTime());
        assertThat(mission.getClearedThumbnail()).isEqualTo(dto.getClearedThumbnail());
        assertThat(mission.getNotClearedThumbnail()).isEqualTo(dto.getNotClearedThumbnail());
    }

    @Test
    void 미션_일부수정_성공() {
        // given
        MissionUpdateDto dto = MissionFixture.미션_업데이트_DTO_일부수정_생성();
        given(missionRepository.findById(anyLong())).willReturn(Optional.of(mission));

        // when
        missionService.updateMission(1L, dto);

        // then - 수정된 필드
        assertThat(mission.getTitle()).isEqualTo("수정된 제목");
        assertThat(mission.getContent()).isEqualTo("수정된 내용");

        // then - 수정되지 않은 필드
        assertThat(mission.getBoothId()).isEqualTo(1L);
        assertThat(mission.getPlace()).isEqualTo("A관 1층");
        assertThat(mission.getTime()).isEqualTo("10:00 ~ 18:00");
        assertThat(mission.getClearedThumbnail()).isEqualTo("cleared.png");
        assertThat(mission.getNotClearedThumbnail()).isEqualTo("not_cleared.png");
    }
}
