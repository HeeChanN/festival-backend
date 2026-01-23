package com.halo.eventer.domain.stamp.dto.mission.request;

import jakarta.validation.constraints.NotNull;

import lombok.Getter;

@Getter
public class MissionsAllShowReqDto {
    @NotNull
    private Boolean showMissions;
}
