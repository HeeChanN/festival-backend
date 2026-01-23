package com.halo.eventer.domain.stamp.dto.stampUser.request;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MissionQrVerifyReqDto {
    @NotNull
    private long missionId;
}
