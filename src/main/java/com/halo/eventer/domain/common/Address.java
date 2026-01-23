package com.halo.eventer.domain.common;

import jakarta.persistence.Embeddable;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Getter
public class Address {
    private String sido;
    private String sigungu;
    private String dongmyun;
    private String roadName;
    private String roadNumber;
    private String buildingName;

    public Address(
            String sido, String sigungu, String dongmyun, String roadName, String roadNumber, String buildingName) {
        this.sido = sido;
        this.sigungu = sigungu;
        this.dongmyun = dongmyun;
        this.roadName = roadName;
        this.roadNumber = roadNumber;
        this.buildingName = buildingName;
    }
}
