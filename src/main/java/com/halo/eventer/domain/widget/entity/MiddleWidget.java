package com.halo.eventer.domain.widget.entity;

import javax.persistence.*;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.widget.BaseWidget;
import com.halo.eventer.domain.widget.dto.middle_widget.MiddleWidgetCreateDto;
import com.halo.eventer.domain.widget.feature.DisplayOrderFeature;
import com.halo.eventer.domain.widget.feature.ImageFeature;
import com.halo.eventer.global.constants.DisplayOrderConstants;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("MIDDLE")
@NoArgsConstructor
@Getter
public class MiddleWidget extends BaseWidget implements DisplayOrderUpdatable {

    @Embedded
    @AttributeOverride(name = "image", column = @Column(name = "image"))
    private ImageFeature imageFeature;

    @Embedded
    @AttributeOverride(name = "displayOrder", column = @Column(name = "display_order"))
    private DisplayOrderFeature displayOrderFeature;

    @Builder
    private MiddleWidget(Festival festival, String name, String url, String image, Integer displayOrder) {
        super(festival, name, url);
        this.imageFeature = ImageFeature.of(image);
        this.displayOrderFeature = DisplayOrderFeature.of(displayOrder);
    }

    public static MiddleWidget from(Festival festival, MiddleWidgetCreateDto middleWidgetCreateDto) {
        return MiddleWidget.builder()
                .festival(festival)
                .name(middleWidgetCreateDto.getName())
                .url(middleWidgetCreateDto.getUrl())
                .image(middleWidgetCreateDto.getImage())
                .displayOrder(DisplayOrderConstants.DISPLAY_ORDER_DEFAULT)
                .build();
    }

    public void updateMiddleWidget(MiddleWidgetCreateDto middleWidgetCreateDto) {
        updateBaseField(middleWidgetCreateDto.getName(), middleWidgetCreateDto.getUrl());
        this.imageFeature = ImageFeature.of(middleWidgetCreateDto.getImage());
    }

    public void updateDisplayOrder(Integer displayOrder) {
        this.displayOrderFeature = DisplayOrderFeature.of(displayOrder);
    }
}
