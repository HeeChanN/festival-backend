package com.halo.eventer.domain.stamp.dto.stampUser;

import java.util.List;

import com.halo.eventer.domain.stamp.StampUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class StampUserGetDto {

    private String uuid;
    private boolean finished;
    private int participantCount;
    private List<UserMissionInfoGetDto> userMissionInfoGetDtos;

    public static StampUserGetDto from(StampUser stampUser) {
        return StampUserGetDto.builder()
                .uuid(stampUser.getUuid())
                .participantCount(stampUser.getParticipantCount())
                .finished(stampUser.isFinished())
                .userMissionInfoGetDtos(UserMissionInfoGetDto.fromEntities(stampUser.getUserMissions()))
                .build();
    }
}
