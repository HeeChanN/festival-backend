package com.halo.eventer.domain.stamp.dto.stampUser;

import java.util.List;

import lombok.Getter;

@Getter
public class UserMissionInfoWithFinishedGetListDto {

    private boolean finished;
    private List<UserMissionInfoGetDto> userMissionInfoGetDtos;

    public UserMissionInfoWithFinishedGetListDto(boolean finished, List<UserMissionInfoGetDto> userMissionInfoGetDtos) {
        this.finished = finished;
        this.userMissionInfoGetDtos = userMissionInfoGetDtos;
    }
}
