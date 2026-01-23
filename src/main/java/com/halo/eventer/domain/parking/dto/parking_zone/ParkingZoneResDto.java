package com.halo.eventer.domain.parking.dto.parking_zone;

import java.util.List;

import com.halo.eventer.domain.parking.dto.parking_lot.ParkingLotSummaryDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ParkingZoneResDto {
    private Long id;
    private String name;
    private Boolean visible;
    private List<ParkingLotSummaryDto> parkingLots;

    public ParkingZoneResDto(Long id, String name, Boolean visible, List<ParkingLotSummaryDto> parkingLots) {
        this.id = id;
        this.name = name;
        this.visible = visible;
        this.parkingLots = parkingLots;
    }
}
