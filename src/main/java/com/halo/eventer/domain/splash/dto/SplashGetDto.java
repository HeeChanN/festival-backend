package com.halo.eventer.domain.splash.dto;

import lombok.Getter;

@Getter
public class SplashGetDto {
    private String background;

    private String top;

    private String center;

    private String bottom;

    public SplashGetDto(String background, String top, String center, String bottom) {
        this.background = background;
        this.top = top;
        this.center = center;
        this.bottom = bottom;
    }
}
