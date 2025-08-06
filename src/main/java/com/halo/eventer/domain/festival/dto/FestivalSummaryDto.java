package com.halo.eventer.domain.festival.dto;

import com.halo.eventer.domain.festival.Festival;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FestivalSummaryDto {

    private Long id;
    private String festivalName;
    private String subAddress;
    private double latitude; // 위도
    private double longitude; // 경도

    public FestivalSummaryDto(Festival festival) {
        this.festivalName = festival.getName();
        this.id = festival.getId();
        this.subAddress = festival.getSubAddress();
        this.latitude = festival.getLatitude();
        this.longitude = festival.getLongitude();
    }

    public FestivalSummaryDto(Long id, String festivalName, String subAddress, double latitude, double longitude) {
        this.id = id;
        this.festivalName = festivalName;
        this.subAddress = subAddress;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
