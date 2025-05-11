package com.halo.eventer.domain.widget.dto.down_widget;

import com.halo.eventer.domain.widget.entity.DownWidget;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DownWidgetResDto {
    private Long id;
    private String name;
    private String url;
    private int displayOrder;

    @Builder
    private DownWidgetResDto(long id, String name, String url, int displayOrder) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.displayOrder = displayOrder;
    }

    public static DownWidgetResDto from(DownWidget downWidget) {
        return DownWidgetResDto.builder()
                .id(downWidget.getId())
                .name(downWidget.getName())
                .url(downWidget.getUrl())
                .displayOrder(downWidget.getDisplayOrderFeature().getDisplayOrder())
                .build();
    }
}
