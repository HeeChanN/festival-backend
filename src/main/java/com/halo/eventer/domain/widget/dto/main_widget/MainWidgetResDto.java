package com.halo.eventer.domain.widget.dto.main_widget;

import com.halo.eventer.domain.widget.Widget;
import com.halo.eventer.domain.widget.properties.MainWidgetProperties;
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

    public static MainWidgetResDto from(Widget widget) {
        MainWidgetProperties props = widget.getTypedProperties(MainWidgetProperties.class);
        return MainWidgetResDto.builder()
                .id(widget.getId())
                .name(widget.getName())
                .url(widget.getUrl())
                .description(props.getDescription())
                .image(props.getImage())
                .build();
    }
}
