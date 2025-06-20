package com.halo.eventer.domain.stamp.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.halo.eventer.domain.stamp.Mission;
import com.halo.eventer.domain.stamp.StampUser;
import com.halo.eventer.domain.stamp.UserMission;
import com.halo.eventer.domain.stamp.fixture.MissionFixture;
import com.halo.eventer.domain.stamp.fixture.StampFixture;
import com.halo.eventer.domain.stamp.fixture.StampUserFixture;

import static org.assertj.core.api.Assertions.*;

@SuppressWarnings("NonAsciiCharacters")
public class UserMissionTest {

    private Mission mission;
    private StampUser stampUser;
    private UserMission userMission;

    @BeforeEach
    void setUp() {
        mission = MissionFixture.미션_엔티티_생성();
        stampUser = StampUserFixture.스탬프유저_엔티티_생성(StampFixture.스탬프_엔티티_생성());
    }

    @Test
    void 유저미션_생성_성공() {
        // given & when
        userMission = UserMission.create(mission, stampUser);

        // then
        assertThat(userMission.getMission()).isEqualTo(mission);
        assertThat(userMission.getStampUser()).isEqualTo(stampUser);
    }

    @Test
    void 유저미션_완료_성공() {
        // given & when
        userMission = UserMission.create(mission, stampUser);
        userMission.markAsComplete();

        // then
        assertThat(userMission.isComplete()).isEqualTo(true);
    }
}
