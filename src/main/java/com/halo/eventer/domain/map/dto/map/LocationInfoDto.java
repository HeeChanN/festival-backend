package com.halo.eventer.domain.map.dto.map;

import com.halo.eventer.domain.map.embedded.LocationInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LocationInfoDto {
    private String address;
    private double latitude;
    private double longitude;

    @Builder
    private LocationInfoDto(String address, double latitude, double longitude) {
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static LocationInfoDto from(LocationInfo locationInfo) {
        return LocationInfoDto.builder()
                .address(locationInfo.getAddress())
                .latitude(locationInfo.getLatitude())
                .longitude(locationInfo.getLongitude())
                .build();
    }
}
