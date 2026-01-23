package com.halo.eventer.domain.stamp.dto.mission.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MissionListResDto {
    private List<MissionBriefResDto> missionList;
}
