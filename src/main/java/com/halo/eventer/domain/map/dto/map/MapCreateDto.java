package com.halo.eventer.domain.map.dto.map;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MapCreateDto {
    private String name;
    private String summary;
    private String content;
    private String thumbnail;
    private String icon;

    private OperationInfoDto operationInfo;
    private LocationInfoDto locationInfo;
    private ButtonInfoDto buttonInfo;
    private List<Long> durationIdsToAdd;

    @Builder
    public MapCreateDto(String name, String summary, String content, String thumbnail, String icon,
                        OperationInfoDto operationInfo, LocationInfoDto locationInfo, ButtonInfoDto buttonInfo,
                        List<Long> durationIdsToAdd) {
        this.name = name;
        this.summary = summary;
        this.content = content;
        this.thumbnail = thumbnail;
        this.icon = icon;
        this.operationInfo = operationInfo;
        this.locationInfo = locationInfo;
        this.buttonInfo = buttonInfo;
        this.durationIdsToAdd = durationIdsToAdd;
    }
}
