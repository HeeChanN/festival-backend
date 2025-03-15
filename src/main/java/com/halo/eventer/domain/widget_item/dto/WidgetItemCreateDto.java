package com.halo.eventer.domain.widget_item.dto;

import com.halo.eventer.domain.image.dto.ImageDto;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WidgetItemCreateDto {

  private String name;
  private String description;
  private String icon;

  private List<ImageDto> images;
}
