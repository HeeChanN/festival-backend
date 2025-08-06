package com.halo.eventer.domain.splash.dto.request;

import jakarta.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class ImageLayerDto {
    @NotBlank(message = "url은 필수 값입니다.")
    private String url;

    @NotBlank(message = "layerType은 필수값입니다.")
    private String layerType;
}
