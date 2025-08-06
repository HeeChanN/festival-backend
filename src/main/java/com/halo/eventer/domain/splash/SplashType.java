package com.halo.eventer.domain.splash;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SplashType {
    BACKGROUND("background"),
    TOP("top"),
    CENTER("center"),
    BOTTOM("bottom"),
    ;
    private final String type;
}
