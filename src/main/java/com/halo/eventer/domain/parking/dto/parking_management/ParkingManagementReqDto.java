package com.halo.eventer.domain.parking.dto.parking_management;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ParkingManagementReqDto {
    private String headerName;

    @NotNull
    @Pattern(regexp = "BASIC|BUTTON|SIMPLE", message = "parkingInfoType must be one of: BASIC, BUTTON, SIMPLE")
    private String parkingInfoType;

    private String title;
    private String description;
    private String buttonName;
    private String buttonTargetUrl;
    private String backgroundImage;

    @NotNull
    private boolean visible;

    @Builder
    public ParkingManagementReqDto(
            String headerName,
            String parkingInfoType,
            String title,
            String description,
            String buttonName,
            String buttonTargetUrl,
            String backgroundImage,
            boolean visible) {
        this.headerName = headerName;
        this.parkingInfoType = parkingInfoType;
        this.title = title;
        this.description = description;
        this.buttonName = buttonName;
        this.buttonTargetUrl = buttonTargetUrl;
        this.backgroundImage = backgroundImage;
        this.visible = visible;
    }
}
