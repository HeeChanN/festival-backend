package com.halo.eventer.domain.map.dto.map;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MapUpdateDto {
    private String name;
    private String summary;
    private String content;
    private String thumbnail;
    private String icon;

    private OperationInfoDto operationInfo;
    private LocationInfoDto locationInfo;
    private ButtonInfoDto buttonInfo;
    private DurationBindingDto durationBinding;
}
