package com.halo.eventer.domain.parking.dto.parking_notice;

import jakarta.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ParkingNoticeReqDto {

    @NotNull
    private String title;

    private String content;

    public ParkingNoticeReqDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
