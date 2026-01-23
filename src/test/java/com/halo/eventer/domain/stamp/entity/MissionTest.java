package com.halo.eventer.domain.stamp.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.stamp.Mission;
import com.halo.eventer.domain.stamp.Stamp;
import com.halo.eventer.domain.stamp.dto.mission.MissionUpdateDto;
import com.halo.eventer.domain.stamp.dto.mission.request.MissionSetReqDto;

import static com.halo.eventer.domain.festival.FestivalFixture.축제_엔티티;
import static com.halo.eventer.domain.stamp.fixture.MissionFixture.미션_생성_DTO_생성;
import static com.halo.eventer.domain.stamp.fixture.MissionFixture.미션_업데이트_DTO_생성;
import static com.halo.eventer.domain.stamp.fixture.StampFixture.스탬프1_생성;
import static org.assertj.core.api.Assertions.*;

@SuppressWarnings("NonAsciiCharacters")
public class MissionTest {

    private Festival festival;
    private Mission mission;
    private Stamp stamp;

    private MissionSetReqDto setDto;
    private MissionUpdateDto updateDto;

    @BeforeEach
    void setUp() {
        festival = 축제_엔티티();
        setDto = 미션_생성_DTO_생성();
        updateDto = 미션_업데이트_DTO_생성();
        mission = Mission.from(setDto);
        stamp = 스탬프1_생성(festival);
    }

    @Test
    void 미션_정보_업데이트된다() {
        // when
        mission.updateMission(updateDto);

        // then
        assertThat(mission.getBoothId()).isEqualTo(updateDto.getBoothId());
        assertThat(mission.getTitle()).isEqualTo(updateDto.getTitle());
        assertThat(mission.getContent()).isEqualTo(updateDto.getContent());
        assertThat(mission.getPlace()).isEqualTo(updateDto.getPlace());
        assertThat(mission.getTime()).isEqualTo(updateDto.getTime());
        assertThat(mission.getClearedThumbnail()).isEqualTo(updateDto.getClearedThumbnail());
        assertThat(mission.getNotClearedThumbnail()).isEqualTo(updateDto.getNotClearedThumbnail());
    }

    @Test
    void 미션에_스탬프_추가된다() {
        // when
        mission.addStamp(stamp);

        // then
        assertThat(mission.getStamp()).isSameAs(stamp);
        assertThat(stamp.getMissions()).contains(mission);
    }

    @Test
    void 이미_스탬프가_있는경우_addStamp_호출시_중복추가된다() {
        // given
        mission.addStamp(stamp);

        // when
        mission.addStamp(stamp);

        // then
        assertThat(stamp.getMissions()).filteredOn(m -> m.equals(mission)).hasSize(2);
    }
}
