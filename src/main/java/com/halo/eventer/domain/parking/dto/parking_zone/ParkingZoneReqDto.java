package com.halo.eventer.domain.parking.dto.parking_zone;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ParkingZoneReqDto {

    @NotEmpty
    private String name;

    @NotNull
    private Boolean visible;
}
