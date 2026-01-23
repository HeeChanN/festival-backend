package com.halo.eventer.domain.stamp.dto.mission.response;

import java.util.List;
import java.util.stream.Collectors;

import com.halo.eventer.domain.stamp.Mission;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MissionSummaryResDto {
    private Long missionId;
    private String title;
    private String clearedThumbnail;
    private String notClearedThumbnail;

    public static List<MissionSummaryResDto> fromEntities(List<Mission> missions) {
        return missions.stream().map(MissionSummaryResDto::from).collect(Collectors.toList());
    }

    public static MissionSummaryResDto from(Mission mission) {
        return new MissionSummaryResDto(
                mission.getId(), mission.getTitle(), mission.getClearedThumbnail(), mission.getNotClearedThumbnail());
    }
}
