package com.halo.eventer.domain.stamp.dto.stampUser;

import java.util.List;

import com.halo.eventer.domain.stamp.StampUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserMissionInfoWithFinishedGetListDto {

    private boolean finished;
    private List<UserMissionInfoGetDto> userMissionInfoGetDtos;

    public static UserMissionInfoWithFinishedGetListDto from(StampUser stampUser) {
        return UserMissionInfoWithFinishedGetListDto.builder()
                .finished(stampUser.isFinished())
                .userMissionInfoGetDtos(UserMissionInfoGetDto.fromEntities(stampUser.getUserMissions()))
                .build();
    }
}
