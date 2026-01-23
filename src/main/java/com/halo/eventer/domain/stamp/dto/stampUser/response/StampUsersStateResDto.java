package com.halo.eventer.domain.stamp.dto.stampUser.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StampUsersStateResDto {
    private Integer totalParticipants;
    private Long totalFinished;
    private List<StampUserSummaryResDto> stampUsers;

    public static StampUsersStateResDto from(
            Integer totalParticipate, Long totalFinished, List<StampUserSummaryResDto> stampUsers) {
        return new StampUsersStateResDto(totalParticipate, totalFinished, stampUsers);
    }
}
