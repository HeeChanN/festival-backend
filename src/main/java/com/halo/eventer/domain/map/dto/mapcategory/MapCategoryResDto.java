package com.halo.eventer.domain.map.dto.mapcategory;

import com.halo.eventer.domain.map.MapCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MapCategoryResDto {
  private Long mapCategoryId;
  private String categoryName;
  private String pin;
  private String icon;
  private int displayOrder;

  @Builder
  private MapCategoryResDto(Long mapCategoryId, String categoryName, String pin, String icon, int displayOrder) {
    this.mapCategoryId = mapCategoryId;
    this.categoryName = categoryName;
    this.pin = pin;
    this.icon = icon;
    this.displayOrder = displayOrder;
  }

  public static MapCategoryResDto from(MapCategory mapCategory) {
    return MapCategoryResDto.builder()
            .mapCategoryId(mapCategory.getId())
            .categoryName(mapCategory.getCategoryName())
            .pin(mapCategory.getPin())
            .icon(mapCategory.getIcon())
            .displayOrder(mapCategory.getDisplayOrder())
            .build();
  }
}
