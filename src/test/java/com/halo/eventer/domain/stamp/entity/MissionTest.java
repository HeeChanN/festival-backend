package com.halo.eventer.domain.stamp.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.halo.eventer.domain.stamp.Mission;
import com.halo.eventer.domain.stamp.Stamp;
import com.halo.eventer.domain.stamp.dto.mission.MissionUpdateDto;
import com.halo.eventer.domain.stamp.dto.stamp.MissionSetDto;
import com.halo.eventer.domain.stamp.fixture.MissionFixture;
import com.halo.eventer.domain.stamp.fixture.StampFixture;

import static org.assertj.core.api.Assertions.*;

@SuppressWarnings("NonAsciiCharacters")
public class MissionTest {

    private Mission mission;
    private Stamp stamp;

    private MissionSetDto setDto;
    private MissionUpdateDto updateDto;

    @BeforeEach
    void setUp() {
        setDto = MissionFixture.미션_생성_DTO_생성();
        updateDto = MissionFixture.미션_업데이트_DTO_생성();

        mission = Mission.from(setDto);
        stamp = StampFixture.스탬프_엔티티_생성();
    }

    @Test
    void 미션_업데이트_성공() {
        // given & when
        mission.updateMission(updateDto);

        // then
        assertThat(mission.getPlace()).isEqualTo(updateDto.getPlace());
        assertThat(mission.getTitle()).isEqualTo(updateDto.getTitle());
        assertThat(mission.getTime()).isEqualTo(updateDto.getTime());
    }

    @Test
    void 미션_스탬프_추가_성공() {
        // given & when
        mission.addStamp(stamp);

        // then
        assertThat(mission.getStamp()).isEqualTo(stamp); // 동등성 검사
    }
}
