package com.halo.eventer.domain.stamp.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.festival.FestivalFixture;
import com.halo.eventer.domain.stamp.Mission;
import com.halo.eventer.domain.stamp.Stamp;
import com.halo.eventer.domain.stamp.StampUser;
import com.halo.eventer.domain.stamp.UserMission;
import com.halo.eventer.domain.stamp.exception.StampClosedException;
import com.halo.eventer.domain.stamp.fixture.MissionFixture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
public class StampTest {

    private Stamp stamp;
    private Festival festival;

    @BeforeEach
    void setUp() {
        festival = FestivalFixture.축제_엔티티();
        stamp = Stamp.create(festival);
    }

    @Test
    void 스탬프_생성() {
        assertThat(stamp.getFestival()).isEqualTo(festival);
        assertThat(stamp.isActive()).isTrue();
        assertThat(stamp.getFinishCount()).isZero();
    }

    @Test
    void 스탬프_활성_확인() {
        stamp.switchActivation();
        assertThat(stamp.isActive()).isFalse();

        stamp.switchActivation();
        assertThat(stamp.isActive()).isTrue();
    }

    @Test
    void 스탬프_완료() {
        stamp.defineFinishCnt(5);
        assertThat(stamp.getFinishCount()).isEqualTo(5);
    }

    @Test
    void validateActivationThrowsException() {
        stamp.switchActivation(); // 비활성화

        assertThatThrownBy(() -> stamp.validateActivation())
                .isInstanceOf(StampClosedException.class)
                .hasMessageContaining(String.valueOf(stamp.getId()));
    }

    @Test
    void assignAllMissionsTo() {
        // given
        Mission mission1 = MissionFixture.미션_엔티티_생성();
        Mission mission2 = MissionFixture.미션_엔티티_생성();

        stamp.getMissions().add(mission1);
        stamp.getMissions().add(mission2);

        StampUser stampUser = new StampUser(stamp, "encryptedPhone", "encryptedName", 1);

        // when
        stamp.assignAllMissionsTo(stampUser);

        // then
        assertThat(stampUser.getUserMissions()).hasSize(2);
        assertThat(stampUser.getUserMissions())
                .extracting(UserMission::getMission)
                .contains(mission1, mission2);
    }
}
