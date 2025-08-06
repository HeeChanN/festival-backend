package com.halo.eventer.domain.widget.entity;

import jakarta.persistence.*;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.widget.BaseWidget;
import com.halo.eventer.domain.widget.dto.main_widget.MainWidgetCreateDto;
import com.halo.eventer.domain.widget.feature.DescriptionFeature;
import com.halo.eventer.domain.widget.feature.ImageFeature;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("MAIN")
@Getter
@NoArgsConstructor
public class MainWidget extends BaseWidget {
    @Embedded
    @AttributeOverride(name = "image", column = @Column(name = "image"))
    private ImageFeature imageFeature;

    @Embedded
    @AttributeOverride(name = "description", column = @Column(name = "description"))
    private DescriptionFeature descriptionFeature;

    @Builder
    private MainWidget(Festival festival, String name, String url, String image, String description) {
        super(festival, name, url);
        this.imageFeature = ImageFeature.of(image);
        this.descriptionFeature = DescriptionFeature.of(description);
        festival.applyBaseWidget(this);
    }

    public static MainWidget of(Festival festival, String name, String url, String image, String description) {
        return MainWidget.builder()
                .festival(festival)
                .name(name)
                .url(url)
                .image(image)
                .description(description)
                .build();
    }

    public static MainWidget from(Festival festival, MainWidgetCreateDto mainWidgetCreateDto) {
        return MainWidget.builder()
                .festival(festival)
                .name(mainWidgetCreateDto.getName())
                .url(mainWidgetCreateDto.getUrl())
                .description(mainWidgetCreateDto.getDescription())
                .image(mainWidgetCreateDto.getImage())
                .build();
    }

    public void updateMainWidget(MainWidgetCreateDto mainWidgetCreateDto) {
        updateBaseField(mainWidgetCreateDto.getName(), mainWidgetCreateDto.getUrl());
        this.imageFeature = ImageFeature.of(mainWidgetCreateDto.getImage());
        this.descriptionFeature = DescriptionFeature.of(mainWidgetCreateDto.getDescription());
    }
}
