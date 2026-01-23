package com.halo.eventer.domain.parking.dto.parking_management;

import java.util.List;

import com.halo.eventer.domain.image.Image;
import com.halo.eventer.domain.image.dto.ImageDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ParkingManagementSubPageResDto {
    private String subPageHeaderName;
    private List<ImageDto> images;

    public ParkingManagementSubPageResDto(String subPageHeaderName, List<Image> images) {
        this.subPageHeaderName = subPageHeaderName;
        this.images = images.stream().map(ImageDto::from).toList();
    }
}
