package com.halo.eventer.domain.stamp.dto.mission.response;

import java.util.List;

import com.halo.eventer.domain.stamp.Mission;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MissionQrDataResDto {
    private Long missionId;
    private String title;

    public static MissionQrDataResDto from(Mission mission) {
        return new MissionQrDataResDto(mission.getId(), mission.getTitle());
    }

    public static List<MissionQrDataResDto> fromEntities(List<Mission> missions) {
        if (missions == null || missions.isEmpty()) {
            return List.of();
        }
        return missions.stream().map(MissionQrDataResDto::from).toList();
    }
}
