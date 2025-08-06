package com.halo.eventer.domain.splash.dto.request;

import java.util.List;

import lombok.Getter;

@Getter
public class UploadImageDto {
    private List<ImageLayerDto> imageLayers;
}
