package com.halo.eventer.domain.parking.dto.parking_lot;

import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CongestionLevelReqDto {

    @NotNull
    private String congestionLevel;

    public CongestionLevelReqDto(String congestionLevel) {
        this.congestionLevel = congestionLevel;
    }
}
