package com.halo.eventer.domain.stamp.dto.mission.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StampMissionClearImageReqDto {
    private boolean showMissionTitle;
    private String clearedThumbnail;
    private String notClearedThumbnail;
}
