package com.halo.eventer.domain.stamp.dto.mission.response;

import com.halo.eventer.domain.stamp.Mission;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MissionShowResDto {
    private Boolean showMission;

    public static MissionShowResDto from(Mission mission) {
        return new MissionShowResDto(mission.getShowMission());
    }
}
