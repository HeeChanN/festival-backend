package com.halo.eventer.domain.stamp.fixture;

import com.halo.eventer.domain.stamp.Mission;
import com.halo.eventer.domain.stamp.StampUser;
import com.halo.eventer.domain.stamp.UserMission;

@SuppressWarnings("NonAsciiCharacters")
public class UserMissionFixture {

    public static UserMission 유저미션_엔티티_생성(StampUser stampUser, Mission mission) {
        return UserMission.create(mission, stampUser);
    }
}
