package com.halo.eventer.domain.festival.dto;

import com.halo.eventer.domain.festival.Festival;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FestivalListDto {

    private Long id;
    private String festivalName;
    private String subAddress;
    private double latitude; // 위도
    private double longitude; // 경도

    public FestivalListDto(Festival festival) {
        this.festivalName = festival.getName();
        this.id = festival.getId();
        this.subAddress = festival.getSubAddress();
        this.latitude = festival.getLatitude();
        this.longitude = festival.getLongitude();
    }
}
