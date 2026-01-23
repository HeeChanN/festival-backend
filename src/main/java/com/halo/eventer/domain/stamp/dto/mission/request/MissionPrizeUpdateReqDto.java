package com.halo.eventer.domain.stamp.dto.mission.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MissionPrizeUpdateReqDto {

    @NotNull
    private long prizeId;

    @Min(1)
    private int requiredCount;

    @NotBlank
    private String prizeDescription;
}
