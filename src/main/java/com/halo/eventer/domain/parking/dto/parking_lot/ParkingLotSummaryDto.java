package com.halo.eventer.domain.parking.dto.parking_lot;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ParkingLotSummaryDto {
    private Long id;
    private String name;
    private String CongestionLevel;
    private Boolean visible;
    private String copyAddress;
    private String displayAddress;
    private double latitude;
    private double longitude;

    public ParkingLotSummaryDto(
            Long id,
            String name,
            String congestionLevel,
            Boolean visible,
            String copyAddress,
            String displayAddress,
            double latitude,
            double longitude) {
        this.id = id;
        this.name = name;
        CongestionLevel = congestionLevel;
        this.visible = visible;
        this.copyAddress = copyAddress;
        this.displayAddress = displayAddress;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
