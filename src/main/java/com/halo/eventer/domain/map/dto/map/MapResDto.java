package com.halo.eventer.domain.map.dto.map;

import com.halo.eventer.domain.duration.Duration;
import com.halo.eventer.domain.duration.dto.DurationResDto;
import com.halo.eventer.domain.duration.DurationMap;
import com.halo.eventer.domain.map.Map;
import com.halo.eventer.domain.map.Menu;
import com.halo.eventer.domain.map.dto.menu.MenuResDto;
import com.halo.eventer.domain.map.enumtype.OperationTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MapResDto {

    private Long mapId;
    private String name;
    private String summary;
    private String content;
    private String thumbnail;
    private String icon;

    private LocationInfoDto locationInfo;
    private OperationInfoDto operationInfo;
    private ButtonInfoDto buttonInfo;

    private String categoryName;
    private List<DurationResDto> durations;

    @Builder
    public MapResDto(Long mapId, String name, String summary, String content, String thumbnail, String icon,
                     LocationInfoDto locationInfo, OperationInfoDto operationInfo,
                     ButtonInfoDto buttonInfo, String categoryName, List<DurationResDto> durations) {
        this.mapId = mapId;
        this.name = name;
        this.summary = summary;
        this.content = content;
        this.thumbnail = thumbnail;
        this.icon = icon;
        this.locationInfo = locationInfo;
        this.operationInfo = operationInfo;
        this.buttonInfo = buttonInfo;
        this.categoryName = categoryName;
        this.durations = durations;
    }

    public static MapResDto of(Map map, List<Duration> durations) {
        return MapResDto.builder()
                .mapId(map.getId())
                .name(map.getName())
                .summary(map.getSummary())
                .content(map.getContent())
                .thumbnail(map.getThumbnail())
                .icon(map.getIcon())
                .locationInfo(LocationInfoDto.from(map.getLocationInfo()))
                .operationInfo(OperationInfoDto.from(map.getOperationInfo()))
                .buttonInfo(ButtonInfoDto.from(map.getButtonInfo()))
                .categoryName(map.getMapCategory().getCategoryName())
                .durations(DurationResDto.fromDurations(durations))
                .build();
    }

    public static MapResDto from(Map map) {
        List<Duration> durations = map.getDurationMaps().stream()
                .map(DurationMap::getDuration)
                .collect(Collectors.toList());
        return MapResDto.builder()
                .mapId(map.getId())
                .name(map.getName())
                .summary(map.getSummary())
                .content(map.getContent())
                .thumbnail(map.getThumbnail())
                .icon(map.getIcon())
                .locationInfo(LocationInfoDto.from(map.getLocationInfo()))
                .operationInfo(OperationInfoDto.from(map.getOperationInfo()))
                .buttonInfo(ButtonInfoDto.from(map.getButtonInfo()))
                .categoryName(map.getMapCategory().getCategoryName())
                .durations(DurationResDto.fromDurations(durations))
                .build();
    }
}
