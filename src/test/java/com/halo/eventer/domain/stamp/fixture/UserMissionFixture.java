package com.halo.eventer.domain.stamp.fixture;

import java.util.List;

import com.halo.eventer.domain.stamp.Mission;
import com.halo.eventer.domain.stamp.StampUser;
import com.halo.eventer.domain.stamp.UserMission;
import com.halo.eventer.domain.stamp.dto.stampUser.UserMissionInfoGetDto;
import com.halo.eventer.domain.stamp.dto.stampUser.UserMissionInfoWithFinishedGetListDto;

@SuppressWarnings("NonAsciiCharacters")
public class UserMissionFixture {

    public static UserMission 유저미션_엔티티_생성(StampUser stampUser, Mission mission) {
        return UserMission.create(mission, stampUser);
    }

    public static UserMissionInfoWithFinishedGetListDto 유저미션_완료여부_응답_생성() {
        List<UserMissionInfoGetDto> missionDtos =
                List.of(new UserMissionInfoGetDto(1L, 11L, false), new UserMissionInfoGetDto(2L, 12L, true));
        return new UserMissionInfoWithFinishedGetListDto(false, missionDtos);
    }
}
