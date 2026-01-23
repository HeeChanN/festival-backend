package com.halo.eventer.domain.stamp.dto.mission.response;

import java.util.List;

import com.halo.eventer.domain.stamp.Mission;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MissionBriefResDto {
    private Long missionId;
    private String title;
    private Boolean showMission;
    private Boolean showMissionTitle;

    public static List<MissionBriefResDto> fromEntities(List<Mission> missions) {
        if (missions == null || missions.isEmpty()) return List.of();
        return missions.stream().map(MissionBriefResDto::from).toList();
    }

    public static MissionBriefResDto from(Mission mission) {
        return new MissionBriefResDto(
                mission.getId(), mission.getTitle(), mission.getShowMission(), mission.getShowTitle());
    }
}
