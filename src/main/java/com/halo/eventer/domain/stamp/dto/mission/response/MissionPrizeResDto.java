package com.halo.eventer.domain.stamp.dto.mission.response;

import java.util.List;

import com.halo.eventer.domain.stamp.StampMissionPrize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MissionPrizeResDto {
    private long id;
    private int requiredCount;
    private String prizeDescription;

    public static MissionPrizeResDto from(StampMissionPrize prize) {
        return new MissionPrizeResDto(prize.getId(), prize.getRequiredCount(), prize.getDescription());
    }

    public static List<MissionPrizeResDto> fromEntities(List<StampMissionPrize> prizes) {
        return prizes.stream().map(MissionPrizeResDto::from).toList();
    }
}
