package com.halo.eventer.domain.parking.dto.parking_management;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ParkingManagementIdResDto {
    private long id;

    public ParkingManagementIdResDto(long id) {
        this.id = id;
    }
}
