package com.halo.eventer.domain.parking.dto.parking_notice;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ParkingNoticeResDto {

    private Long id;
    private String title;
    private String content;
    private Boolean visible;

    public ParkingNoticeResDto(Long id, String title, String content, Boolean visible) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.visible = visible;
    }
}
