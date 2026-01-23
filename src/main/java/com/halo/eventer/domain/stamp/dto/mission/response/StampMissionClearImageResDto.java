package com.halo.eventer.domain.stamp.dto.mission.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StampMissionClearImageResDto {
    private Boolean showMissionTitle;
    private String clearedThumbnail;
    private String notClearedThumbnail;

    public static StampMissionClearImageResDto from(
            boolean showMissionName, String clearedThumbnail, String notClearedThumbnail) {
        return new StampMissionClearImageResDto(showMissionName, clearedThumbnail, notClearedThumbnail);
    }
}
