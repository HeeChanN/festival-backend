package com.halo.eventer.domain.stamp.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.halo.eventer.domain.stamp.Mission;
import com.halo.eventer.domain.stamp.StampUser;
import com.halo.eventer.domain.stamp.UserMission;

import static com.halo.eventer.domain.stamp.fixture.MissionFixture.미션_엔티티_생성;
import static com.halo.eventer.domain.stamp.fixture.StampUserFixture.스탬프유저1_생성;
import static org.assertj.core.api.Assertions.*;

@SuppressWarnings("NonAsciiCharacters")
public class UserMissionTest {

    private Mission mission;
    private StampUser stampUser;
    private UserMission userMission;

    @BeforeEach
    void setUp() {
        mission = 미션_엔티티_생성();
        stampUser = 스탬프유저1_생성();
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
        assertThat(userMission.getComplete()).isTrue();
    }

    @Test
    void 유저미션_이미완료된상태_다시완료처리해도_변화없음() {
        userMission = UserMission.create(mission, stampUser);
        userMission.markAsComplete();

        boolean before = userMission.getComplete();

        // when
        userMission.markAsComplete();

        // then
        assertThat(userMission.getComplete()).isEqualTo(before).isTrue();
    }
}
