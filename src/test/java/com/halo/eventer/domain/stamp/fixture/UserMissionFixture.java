package com.halo.eventer.domain.stamp.fixture;

import com.halo.eventer.domain.stamp.Mission;
import com.halo.eventer.domain.stamp.StampUser;
import com.halo.eventer.domain.stamp.UserMission;

import static org.springframework.test.util.ReflectionTestUtils.setField;

@SuppressWarnings("NonAsciiCharacters")
public class UserMissionFixture {

    public static UserMission 유저미션_엔티티_생성(Long id, StampUser stampUser, Mission mission) {
        UserMission userMission = UserMission.create(mission, stampUser);
        setField(userMission, "id", id);
        return userMission;
    }
}
