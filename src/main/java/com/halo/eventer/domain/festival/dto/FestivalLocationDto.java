package com.halo.eventer.domain.festival.dto;

import com.halo.eventer.domain.festival.Festival;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class FestivalLocationDto {
    private String sido;
    private String sigungu;
    private String dongmyun;
    private String roadName;
    private String roadNumber;
    private String buildingName;
    private double longitude;
    private double latitude;

    public FestivalLocationDto(double longitude, double latitude) {}

    public FestivalLocationDto(Festival festival) {
        if (festival.getAddress() != null) {
            this.sido = festival.getAddress().getSido();
            this.sigungu = festival.getAddress().getSigungu();
            this.dongmyun = festival.getAddress().getDongmyun();
            this.roadName = festival.getAddress().getRoadName();
            this.roadNumber = festival.getAddress().getRoadNumber();
            this.buildingName = festival.getAddress().getBuildingName();
        }
        this.longitude = festival.getLongitude();
        this.latitude = festival.getLatitude();
    }
}
