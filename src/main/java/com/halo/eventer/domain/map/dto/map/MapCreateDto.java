package com.halo.eventer.domain.map.dto.map;

import java.util.List;

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
}
