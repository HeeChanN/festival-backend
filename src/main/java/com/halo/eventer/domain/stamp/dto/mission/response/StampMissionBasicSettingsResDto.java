package com.halo.eventer.domain.stamp.dto.mission.response;

import java.util.List;

import com.halo.eventer.domain.stamp.Stamp;
import com.halo.eventer.domain.stamp.dto.stamp.enums.MissionDetailsDesignLayout;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StampMissionBasicSettingsResDto {
    private Integer missionCount;
    private MissionDetailsDesignLayout missionDetailsDesignLayout;
    private List<MissionPrizeResDto> prizes;

    public static StampMissionBasicSettingsResDto from(Stamp stamp) {
        return new StampMissionBasicSettingsResDto(
                stamp.getMissionCount(),
                stamp.getDefaultDetailLayout(),
                stamp.getPrizes().stream().map(MissionPrizeResDto::from).toList());
    }
}
