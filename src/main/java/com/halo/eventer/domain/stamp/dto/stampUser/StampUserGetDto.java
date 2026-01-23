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
public class StampUserGetDto {

    private String uuid;
    private boolean finished;
    private int participantCount;
    private List<UserMissionInfoGetDto> userMissionInfoGetDtos;

    public static StampUserGetDto from(StampUser stampUser) {
        return StampUserGetDto.builder()
                .uuid(stampUser.getUuid())
                .participantCount(stampUser.getParticipantCount())
                .finished(stampUser.getFinished())
                .userMissionInfoGetDtos(UserMissionInfoGetDto.fromEntities(stampUser.getUserMissions()))
                .build();
    }
}
