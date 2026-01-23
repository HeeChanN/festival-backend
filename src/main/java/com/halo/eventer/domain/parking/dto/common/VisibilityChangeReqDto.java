package com.halo.eventer.domain.parking.dto.common;

import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VisibilityChangeReqDto {

    @NotNull
    private Boolean visible;

    public VisibilityChangeReqDto(Boolean visible) {
        this.visible = visible;
    }
}
