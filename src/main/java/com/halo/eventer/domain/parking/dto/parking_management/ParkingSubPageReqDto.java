package com.halo.eventer.domain.parking.dto.parking_management;

import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ParkingSubPageReqDto {

    @NotNull
    private String subPageHeaderName;

    public ParkingSubPageReqDto(String subPageHeaderName) {
        this.subPageHeaderName = subPageHeaderName;
    }
}
