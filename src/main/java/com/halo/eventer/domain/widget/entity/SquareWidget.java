package com.halo.eventer.domain.widget.entity;

import javax.persistence.*;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.widget.BaseWidget;
import com.halo.eventer.domain.widget.dto.square_widget.SquareWidgetCreateDto;
import com.halo.eventer.domain.widget.feature.DescriptionFeature;
import com.halo.eventer.domain.widget.feature.DisplayOrderFeature;
import com.halo.eventer.domain.widget.feature.ImageFeature;
import com.halo.eventer.global.constants.DisplayOrderConstants;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("SQUARE")
@NoArgsConstructor
@Getter
public class SquareWidget extends BaseWidget implements DisplayOrderUpdatable {

    @Embedded
    @AttributeOverride(name = "image", column = @Column(name = "image"))
    private ImageFeature imageFeature;

    @Embedded
    @AttributeOverride(name = "description", column = @Column(name = "description"))
    private DescriptionFeature descriptionFeature;

    @Embedded
    @AttributeOverride(name = "displayOrder", column = @Column(name = "display_order"))
    private DisplayOrderFeature displayOrderFeature;

    @Builder
    private SquareWidget(
            Festival festival, String name, String url, String image, String description, Integer displayOrder) {
        super(festival, name, url);
        this.imageFeature = ImageFeature.of(image);
        this.descriptionFeature = DescriptionFeature.of(description);
        this.displayOrderFeature = DisplayOrderFeature.of(displayOrder);
    }

    public static SquareWidget from(Festival festival, SquareWidgetCreateDto squareWidgetCreateDto) {
        return SquareWidget.builder()
                .festival(festival)
                .name(squareWidgetCreateDto.getName())
                .url(squareWidgetCreateDto.getUrl())
                .description(squareWidgetCreateDto.getDescription())
                .image(squareWidgetCreateDto.getImage())
                .displayOrder(DisplayOrderConstants.DISPLAY_ORDER_DEFAULT)
                .build();
    }

    public void updateSquareWidget(SquareWidgetCreateDto squareWidgetCreateDto) {
        updateBaseField(squareWidgetCreateDto.getName(), squareWidgetCreateDto.getUrl());
        this.descriptionFeature = DescriptionFeature.of(squareWidgetCreateDto.getDescription());
        this.imageFeature = ImageFeature.of(squareWidgetCreateDto.getImage());
    }

    public void updateDisplayOrder(Integer displayOrder) {
        this.displayOrderFeature = DisplayOrderFeature.of(displayOrder);
    }
}
