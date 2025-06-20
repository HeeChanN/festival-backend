package com.halo.eventer.domain.stamp.dto.stamp;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MissionSetDto {
    private Long boothId;
    private String title;
    private String content;
    private String place;
    private String time;
    private String clearedThumbnail;
    private String notClearedThumbnail;

    public MissionSetDto(
            Long boothId,
            String title,
            String content,
            String place,
            String time,
            String clearedThumbnail,
            String notClearedThumbnail) {
        this.boothId = boothId;
        this.title = title;
        this.content = content;
        this.place = place;
        this.time = time;
        this.clearedThumbnail = clearedThumbnail;
        this.notClearedThumbnail = notClearedThumbnail;
    }
}
