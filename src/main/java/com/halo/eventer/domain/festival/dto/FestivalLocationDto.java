package com.halo.eventer.domain.festival.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class FestivalLocationDto {
    private double longitude;
    private double latitude;

    public FestivalLocationDto(double longitude, double latitude) {}
}
