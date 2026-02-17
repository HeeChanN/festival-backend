package com.halo.eventer.domain.widget.dto.square_widget;

import com.halo.eventer.domain.widget.Widget;
import com.halo.eventer.domain.widget.properties.SquareWidgetProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SquareWidgetResDto {
    private Long id;
    private String name;
    private String description;
    private String icon;
    private String url;
    private Integer displayOrder;

    @Builder
    private SquareWidgetResDto(
            Long id, String name, String description, String icon, String url, Integer displayOrder) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.url = url;
        this.displayOrder = displayOrder;
    }

    public static SquareWidgetResDto from(Widget widget) {
        SquareWidgetProperties props = widget.getTypedProperties(SquareWidgetProperties.class);
        return SquareWidgetResDto.builder()
                .id(widget.getId())
                .name(widget.getName())
                .url(widget.getUrl())
                .description(props.getDescription())
                .icon(props.getImage())
                .displayOrder(widget.getDisplayOrder())
                .build();
    }
}
