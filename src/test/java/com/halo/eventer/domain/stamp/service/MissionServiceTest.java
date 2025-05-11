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
import com.halo.eventer.domain.stamp.dto.mission.MissionDetailGetDto;
import com.halo.eventer.domain.stamp.dto.mission.MissionUpdateDto;
import com.halo.eventer.domain.stamp.exception.MissionNotFoundException;
import com.halo.eventer.domain.stamp.repository.MissionRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@SuppressWarnings("NonAsciiCharacters")
public class MissionServiceTest {

    @Mock
    private MissionRepository missionRepository;

    @InjectMocks
    private MissionService missionService;

    private Mission mission;
    private MissionUpdateDto missionUpdateDto;

    @BeforeEach
    public void setUp() {
        mission = setUpMission();
        missionUpdateDto = setUpMissionUpdateDto();
    }

    @Test
    void 미션조회_성공() {
        // given
        given(missionRepository.findById(1L)).willReturn(Optional.of(mission));

        // when
        MissionDetailGetDto result = missionService.getMission(1L);

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(mission);
    }

    @Test
    void 미션조회_실패() {
        // given
        given(missionRepository.findById(1L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> missionService.getMission(1L)).isInstanceOf(MissionNotFoundException.class);
    }

    @Test
    void 미션수정_성공() {
        // given
        given(missionRepository.findById(1L)).willReturn(Optional.of(mission));

        // when
        missionService.updateMission(mission.getId(), missionUpdateDto);
        MissionDetailGetDto result = missionService.getMission(1L);

        // then
        assertThat(result)
                .usingRecursiveComparison()
                .comparingOnlyFields("boothId")
                .isEqualTo(mission);
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

    private MissionUpdateDto setUpMissionUpdateDto() {
        MissionUpdateDto dto = new MissionUpdateDto();
        setField(dto, "boothId", 2L);
        return dto;
    }
}
