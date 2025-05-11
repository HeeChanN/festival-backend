package com.halo.eventer.domain.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.*;

import com.halo.eventer.domain.duration.Duration;
import com.halo.eventer.domain.duration.DurationMap;
import com.halo.eventer.domain.map.dto.map.MapCreateDto;
import com.halo.eventer.domain.map.dto.map.MapUpdateDto;
import com.halo.eventer.domain.map.embedded.ButtonInfo;
import com.halo.eventer.domain.map.embedded.LocationInfo;
import com.halo.eventer.domain.map.embedded.OperationInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Map {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "summary")
    private String summary;

    @Column(name = "content", length = 2000)
    private String content;

    @Column(name = "thumbnail")
    private String thumbnail;

    @Column(name = "icon", nullable = true)
    private String icon;

    @Embedded
    private LocationInfo locationInfo;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "hours", column = @Column(name = "operation_hours")),
        @AttributeOverride(name = "type", column = @Column(name = "operation_type"))
    })
    private OperationInfo operationInfo;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "name", column = @Column(name = "button_name")),
        @AttributeOverride(name = "url", column = @Column(name = "button_url")),
        @AttributeOverride(name = "image", column = @Column(name = "button_image", length = 1000))
    })
    private ButtonInfo buttonInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mapCategory_id")
    private MapCategory mapCategory;

    @OneToMany(mappedBy = "map", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Menu> menus = new ArrayList<>();

    @OneToMany(mappedBy = "map", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DurationMap> durationMaps = new ArrayList<>();

    @Builder
    private Map(
            String name,
            String summary,
            String content,
            String thumbnail,
            String icon,
            LocationInfo locationInfo,
            OperationInfo operationInfo,
            ButtonInfo buttonInfo,
            MapCategory mapCategory) {
        this.name = name;
        this.summary = summary;
        this.content = content;
        this.thumbnail = thumbnail;
        this.icon = icon;
        this.locationInfo = locationInfo;
        this.operationInfo = operationInfo;
        this.buttonInfo = buttonInfo;
        this.mapCategory = mapCategory;
    }

    public static Map of(MapCreateDto mapCreateDto, MapCategory mapCategory) {
        return Map.builder()
                .name(mapCreateDto.getName())
                .summary(mapCreateDto.getSummary())
                .content(mapCreateDto.getContent())
                .thumbnail(mapCreateDto.getThumbnail())
                .icon(mapCreateDto.getIcon())
                .locationInfo(LocationInfo.from(mapCreateDto.getLocationInfo()))
                .operationInfo(OperationInfo.from(mapCreateDto.getOperationInfo()))
                .buttonInfo(ButtonInfo.from(mapCreateDto.getButtonInfo()))
                .mapCategory(mapCategory)
                .build();
    }

    public void updateMap(MapUpdateDto mapUpdateDto, MapCategory mapCategory, List<Duration> allDurations) {
        this.name = mapUpdateDto.getName();
        this.summary = mapUpdateDto.getSummary();
        this.content = mapUpdateDto.getContent();
        this.thumbnail = mapUpdateDto.getThumbnail();
        this.icon = mapUpdateDto.getIcon();
        this.locationInfo = LocationInfo.from(mapUpdateDto.getLocationInfo());
        this.operationInfo = OperationInfo.from(mapUpdateDto.getOperationInfo());
        this.buttonInfo = ButtonInfo.from(mapUpdateDto.getButtonInfo());
        this.mapCategory = mapCategory;
        updateDurations(
                mapUpdateDto.getDurationBinding().getIdsToAdd(),
                mapUpdateDto.getDurationBinding().getIdsToRemove(),
                allDurations);
    }

    private void updateDurations(
            List<Long> durationIdsToAdd, List<Long> durationIdsToRemove, List<Duration> allDurations) {
        Set<Long> currentIds =
                this.durationMaps.stream().map(dm -> dm.getDuration().getId()).collect(Collectors.toSet());

        List<DurationMap> toAdd = allDurations.stream()
                .filter(duration ->
                        durationIdsToAdd.contains(duration.getId()) && !currentIds.contains(duration.getId()))
                .map(d -> DurationMap.of(d, this))
                .collect(Collectors.toList());

        List<DurationMap> toRemove = this.durationMaps.stream()
                .filter(dm -> durationIdsToRemove.contains(dm.getDuration().getId()))
                .collect(Collectors.toList());

        this.durationMaps.removeAll(toRemove);
        this.durationMaps.addAll(toAdd);
    }
}
