package com.halo.eventer.domain.stamp.dto.mission.response;

import com.halo.eventer.domain.stamp.Mission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MissionDetailGetResDto {
    private Long boothId;
    private String title;
    private String content;
    private String place;
    private String time;
    private String clearedThumbnail;
    private String notClearedThumbnail;

    public static MissionDetailGetResDto from(Mission mission) {
        return MissionDetailGetResDto.builder()
                .boothId(mission.getBoothId())
                .title(mission.getTitle())
                .content(mission.getContent())
                .place(mission.getPlace())
                .time(mission.getTime())
                .clearedThumbnail(mission.getClearedThumbnail())
                .notClearedThumbnail(mission.getNotClearedThumbnail())
                .build();
    }
}
