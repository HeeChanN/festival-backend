package com.halo.eventer.domain.stamp.dto.mission.request;

import com.halo.eventer.domain.stamp.dto.stamp.enums.MissionDetailsDesignLayout;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MissionBasicSettingsReqDto {

    private Integer missionCount;

    private MissionDetailsDesignLayout missionDetailsDesignLayout;
}
