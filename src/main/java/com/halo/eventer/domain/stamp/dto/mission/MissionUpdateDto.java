package com.halo.eventer.domain.stamp.dto.mission;

import lombok.Getter;

@Getter
public class MissionUpdateDto {
    private Long boothId;
    private String title;
    private String content;
    private String place;
    private String time;
    private String clearedThumbnail;
    private String notClearedThumbnail;
}
