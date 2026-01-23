package com.halo.eventer.domain.stamp.dto.mission.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MissionSetReqDto {
    @NotNull
    @Positive
    private long boothId;

    @NotEmpty
    private String title;

    @NotEmpty
    private String content;

    @NotEmpty
    private String place;

    @NotEmpty
    private String time;

    @NotEmpty
    private String clearedThumbnail;

    @NotEmpty
    private String notClearedThumbnail;
}
