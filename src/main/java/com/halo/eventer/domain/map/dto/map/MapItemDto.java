package com.halo.eventer.domain.map.dto.map;

import java.util.List;
import java.util.stream.Collectors;

import com.halo.eventer.domain.duration.dto.DurationResDto;
import com.halo.eventer.domain.map.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MapItemDto {
    private Long id;
    private String name;
    private String icon;
    private String operationTime;
    private LocationInfoDto locationInfo;
    private String categoryName;
    private List<DurationResDto> durationResDto;

    public MapItemDto(Map map) {
        this.id = map.getId();
        this.name = map.getName();
        this.icon = map.getIcon();
        this.operationTime = map.getOperationInfo().getType().name();
        this.locationInfo = LocationInfoDto.from(map.getLocationInfo());
        this.categoryName = map.getMapCategory().getCategoryName();
        this.durationResDto = map.getDurationMaps().stream()
                .map(o -> new DurationResDto(o.getDuration()))
                .collect(Collectors.toList());
    }
}
