package com.halo.eventer.domain.widget.dto.middle_widget;

import com.halo.eventer.domain.widget.Widget;
import com.halo.eventer.domain.widget.properties.MiddleWidgetProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MiddleWidgetResDto {
    private Long id;
    private String name;
    private String url;
    private String image;
    private int displayOrder;

    @Builder
    private MiddleWidgetResDto(long id, String name, String url, String image, int displayOrder) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.image = image;
        this.displayOrder = displayOrder;
    }

    public static MiddleWidgetResDto from(Widget widget) {
        MiddleWidgetProperties props = widget.getTypedProperties(MiddleWidgetProperties.class);
        return MiddleWidgetResDto.builder()
                .id(widget.getId())
                .name(widget.getName())
                .url(widget.getUrl())
                .image(props.getImage())
                .displayOrder(widget.getDisplayOrder())
                .build();
    }
}
