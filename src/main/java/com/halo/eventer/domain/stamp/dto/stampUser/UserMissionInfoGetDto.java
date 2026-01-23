package com.halo.eventer.domain.stamp.dto.stampUser;

import java.util.List;
import java.util.stream.Collectors;

import com.halo.eventer.domain.stamp.UserMission;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserMissionInfoGetDto {

    private Long userMissionId;
    private Long missionId;
    private boolean clear;

    public static List<UserMissionInfoGetDto> fromEntities(List<UserMission> userMissions) {
        return userMissions.stream()
                .map(um -> new UserMissionInfoGetDto(um.getId(), um.getMission().getId(), um.getComplete()))
                .collect(Collectors.toList());
    }
}
