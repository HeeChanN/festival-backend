package com.halo.eventer.domain.stamp.dto.stamp;

import java.util.List;

import lombok.Getter;

@Getter
public class MissionSummaryGetListDto {
    private List<MissionSummaryGetDto> missionSummaryGetDtos;

    public MissionSummaryGetListDto() {}

    public MissionSummaryGetListDto(List<MissionSummaryGetDto> missionSummaryGetDtos) {
        this.missionSummaryGetDtos = missionSummaryGetDtos;
    }
}
