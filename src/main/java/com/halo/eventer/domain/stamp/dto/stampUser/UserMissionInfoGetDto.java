package com.halo.eventer.domain.stamp.dto.stampUser;

import java.util.List;
import java.util.stream.Collectors;

import com.halo.eventer.domain.stamp.UserMission;
import lombok.Getter;

@Getter
public class UserMissionInfoGetDto {
    private Long userMissionId;
    private Long missionId;
    private boolean clear;

    public UserMissionInfoGetDto(Long userMissionId, Long missionId, boolean clear) {
        this.userMissionId = userMissionId;
        this.missionId = missionId;
        this.clear = clear;
    }

    public static List<UserMissionInfoGetDto> fromEntities(List<UserMission> userMissions) {
        return userMissions.stream()
                .map(um -> new UserMissionInfoGetDto(um.getId(), um.getMission().getId(), um.isComplete()))
                .collect(Collectors.toList());
    }
}
