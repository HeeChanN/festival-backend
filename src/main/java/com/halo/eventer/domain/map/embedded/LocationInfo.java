package com.halo.eventer.domain.map.embedded;

import javax.persistence.Embeddable;

import com.halo.eventer.domain.map.dto.map.LocationInfoDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class LocationInfo {
    private String address;
    private double latitude;
    private double longitude;

    @Builder
    private LocationInfo(String address, double latitude, double longitude) {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static LocationInfo from(LocationInfoDto locationInfoDto) {
        return LocationInfo.builder()
                .address(locationInfoDto.getAddress())
                .latitude(locationInfoDto.getLatitude())
                .longitude(locationInfoDto.getLongitude())
                .build();
    }

    public static LocationInfo of(String address, double latitude, double longitude) {
        return LocationInfo.builder()
                .address(address)
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }
}
