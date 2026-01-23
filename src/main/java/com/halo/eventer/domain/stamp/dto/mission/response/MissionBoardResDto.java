package com.halo.eventer.domain.stamp.dto.mission.response;

import java.util.Comparator;
import java.util.List;

import com.halo.eventer.domain.stamp.UserMission;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MissionBoardResDto {
    private int clearCount;
    private int totalCount;
    private boolean finished;
    private List<MissionIconRes> missions;

    public static MissionBoardResDto from(List<UserMission> list, boolean finished) {
        int total = list.size();
        int done = (int) list.stream().filter(UserMission::getComplete).count();
        return new MissionBoardResDto(
                done,
                total,
                finished,
                list.stream()
                        .sorted(Comparator.comparing(um -> um.getMission().getId()))
                        .map(MissionIconRes::from)
                        .toList());
    }
}
