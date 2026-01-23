package com.halo.eventer.domain.stamp.dto.stamp.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ButtonAction {
    QR_CAMERA,
    NFC_SCAN,
    OPEN_URL,
    OPEN_NEW_PAGE,
    OPEN_POPUP,
    ;
}
