package com.halo.eventer.domain.map.dto.mapcategory;

import com.halo.eventer.domain.map.MapCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MapCategoryImageDto {
    private String pin;
    private String icon;

    @Builder
    private MapCategoryImageDto(String pin, String icon) {
        this.pin = pin;
        this.icon = icon;
    }

    public static MapCategoryImageDto from(MapCategory mapCategory) {
        return MapCategoryImageDto.builder()
                .pin(mapCategory.getPin())
                .icon(mapCategory.getIcon())
                .build();
    }

    public static MapCategoryImageDto of(String pin, String icon) {
        return MapCategoryImageDto.builder().pin(pin).icon(icon).build();
    }
}
