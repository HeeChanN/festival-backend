package com.halo.eventer.domain.widget.entity;

import jakarta.persistence.*;

import com.halo.eventer.domain.festival.Festival;
import com.halo.eventer.domain.widget.BaseWidget;
import com.halo.eventer.domain.widget.dto.down_widget.DownWidgetCreateDto;
import com.halo.eventer.domain.widget.feature.DisplayOrderFeature;
import com.halo.eventer.global.constants.DisplayOrderConstants;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("DOWN")
@NoArgsConstructor
@Getter
public class DownWidget extends BaseWidget implements DisplayOrderUpdatable {

    @Embedded
    @AttributeOverride(name = "displayOrder", column = @Column(name = "display_order"))
    private DisplayOrderFeature displayOrderFeature;

    @Builder
    private DownWidget(Festival festival, String name, String url, Integer displayOrder) {
        super(festival, name, url);
        this.displayOrderFeature = DisplayOrderFeature.of(displayOrder);
        festival.applyBaseWidget(this);
    }

    public static DownWidget of(Festival festival, String name, String url, Integer displayOrder) {
        return DownWidget.builder()
                .festival(festival)
                .name(name)
                .url(url)
                .displayOrder(displayOrder)
                .build();
    }

    public static DownWidget from(Festival festival, DownWidgetCreateDto downWidgetCreateDto) {
        return DownWidget.builder()
                .festival(festival)
                .name(downWidgetCreateDto.getName())
                .url(downWidgetCreateDto.getUrl())
                .displayOrder(DisplayOrderConstants.DISPLAY_ORDER_DEFAULT)
                .build();
    }

    public void updateDownWidget(DownWidgetCreateDto downWidgetCreateDto) {
        updateBaseField(downWidgetCreateDto.getName(), downWidgetCreateDto.getUrl());
    }

    public void updateDisplayOrder(Integer displayOrder) {
        this.displayOrderFeature = DisplayOrderFeature.of(displayOrder);
    }
}
