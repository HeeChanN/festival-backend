package com.halo.eventer.domain.stamp.dto.stamp;

import com.halo.eventer.domain.stamp.Mission;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MissionSummaryGetDto {
  private Long missionId;
  private String title;
  private String clearedThumbnail;
  private String notClearedThumbnail;

  public static List<MissionSummaryGetDto> fromEntities(List<Mission> missions){
    return missions.stream()
            .map(MissionSummaryGetDto::from)
            .collect(Collectors.toList());
  }

  public static MissionSummaryGetDto from(Mission mission) {
    return new MissionSummaryGetDto(
            mission.getId(),
            mission.getTitle(),
            mission.getClearedThumbnail(),
            mission.getNotClearedThumbnail()
    );
  }
}
