package com.halo.eventer.domain.stamp.dto.stampUser;

import java.util.List;

import com.halo.eventer.domain.stamp.StampUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserMissionInfoWithFinishedGetListDto {

    private boolean finished;
    private List<UserMissionInfoGetDto> userMissionInfoGetDtos;

    public static UserMissionInfoWithFinishedGetListDto from(StampUser stampUser) {
        return UserMissionInfoWithFinishedGetListDto.builder()
                .finished(stampUser.getFinished())
                .userMissionInfoGetDtos(UserMissionInfoGetDto.fromEntities(stampUser.getUserMissions()))
                .build();
    }
}
