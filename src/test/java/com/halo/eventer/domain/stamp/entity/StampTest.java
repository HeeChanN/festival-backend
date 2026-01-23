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

import static com.halo.eventer.domain.stamp.fixture.MissionFixture.미션1_생성;
import static com.halo.eventer.domain.stamp.fixture.MissionFixture.미션2_생성;
import static com.halo.eventer.domain.stamp.fixture.StampUserFixture.스탬프유저1_생성;
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
        // given & when & then
        assertThat(stamp.getFestival()).isEqualTo(festival);
        assertThat(stamp.getActive()).isTrue();
        assertThat(stamp.getFinishCount()).isZero();
    }

    @Test
    void 스탬프_활성_토글_성공() {
        // given & when
        stamp.switchActivation();

        // then
        assertThat(stamp.getActive()).isFalse();
    }

    @Test
    void 스탬프_비활성_토글_성공() {
        // when
        stamp.switchActivation();
        stamp.switchActivation();

        // then
        assertThat(stamp.getActive()).isTrue();
    }

    @Test
    void 스탬프_완료횟수_정의_성공() {
        // given & when
        stamp.defineFinishCnt(5);

        // then
        assertThat(stamp.getFinishCount()).isEqualTo(5);
    }

    @Test
    void 스탬프_비활성_상태_검증시_예외발생() {
        // given
        stamp.switchActivation();

        // when & then
        assertThatThrownBy(() -> stamp.validateActivation())
                .isInstanceOf(StampClosedException.class)
                .hasMessageContaining(String.valueOf(stamp.getId()));
    }

    @Test
    void 스탬프에_미션_추가_성공() {
        // given
        Mission mission1 = 미션1_생성();
        Mission mission2 = 미션2_생성();

        // when
        mission1.addStamp(stamp);
        mission2.addStamp(stamp);

        // then
        assertThat(stamp.getMissions()).hasSize(2).contains(mission1, mission2);
    }

    @Test
    void 유저에게_미션할당_성공() {
        // given
        Mission mission1 = 미션1_생성();
        Mission mission2 = 미션2_생성();
        StampUser stampUser = 스탬프유저1_생성();
        mission1.addStamp(stamp);
        mission2.addStamp(stamp);
        stampUser.addStamp(stamp);

        // when
        stamp.assignAllMissionsTo(stampUser);

        // then
        assertThat(stampUser.getUserMissions())
                .hasSize(2)
                .extracting(UserMission::getMission)
                .containsExactlyInAnyOrder(mission1, mission2);
    }
}
