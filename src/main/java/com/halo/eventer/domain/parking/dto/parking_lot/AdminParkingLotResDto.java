package com.halo.eventer.domain.parking.dto.parking_lot;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminParkingLotResDto {
    private Long id;
    private String name;
    private String sido;
    private String sigungu;
    private String dongmyun;
    private String roadName;
    private String roadNumber;
    private String buildingName;
    private double latitude;
    private double longitude;

    public AdminParkingLotResDto(
            Long id,
            String name,
            String sido,
            String sigungu,
            String dongmyun,
            String roadName,
            String roadNumber,
            String buildingName,
            double latitude,
            double longitude) {
        this.id = id;
        this.name = name;
        this.sido = sido;
        this.sigungu = sigungu;
        this.dongmyun = dongmyun;
        this.roadName = roadName;
        this.roadNumber = roadNumber;
        this.buildingName = buildingName;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
