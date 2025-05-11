package com.halo.eventer.domain.widget.dto.middle_widget;

import com.halo.eventer.domain.widget.entity.MiddleWidget;
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

    public static MiddleWidgetResDto from(MiddleWidget middleWidget) {
        return MiddleWidgetResDto.builder()
                .id(middleWidget.getId())
                .name(middleWidget.getName())
                .url(middleWidget.getUrl())
                .image(middleWidget.getImageFeature().getImage())
                .displayOrder(middleWidget.getDisplayOrderFeature().getDisplayOrder())
                .build();
    }
}
