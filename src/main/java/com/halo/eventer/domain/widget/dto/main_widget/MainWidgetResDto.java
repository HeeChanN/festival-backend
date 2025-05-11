package com.halo.eventer.domain.widget.dto.main_widget;

import com.halo.eventer.domain.widget.entity.MainWidget;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MainWidgetResDto {
    private Long id;
    private String name;
    private String url;
    private String image;
    private String description;

    @Builder
    private MainWidgetResDto(long id, String name, String url, String image, String description) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.image = image;
        this.description = description;
    }

    public static MainWidgetResDto from(MainWidget mainWidget) {
        return MainWidgetResDto.builder()
                .id(mainWidget.getId())
                .name(mainWidget.getName())
                .url(mainWidget.getUrl())
                .description(mainWidget.getDescriptionFeature().getDescription())
                .image(mainWidget.getImageFeature().getImage())
                .build();
    }
}
