package com.halo.eventer.domain.widget_item.dto;

import com.halo.eventer.domain.widget_item.WidgetItem;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WidgetItemResDto {
    private Long widgetItemId;
    private String name;
    private String description;
    private String icon;

    @Builder
    private WidgetItemResDto(Long widgetItemId, String name, String description, String icon) {
        this.widgetItemId = widgetItemId;
        this.name = name;
        this.description = description;
        this.icon = icon;
    }

    public static WidgetItemResDto from(WidgetItem widgetItem) {
        return WidgetItemResDto.builder()
                .widgetItemId(widgetItem.getId())
                .name(widgetItem.getName())
                .description(widgetItem.getDescription())
                .icon(widgetItem.getIcon())
                .build();
    }
}
