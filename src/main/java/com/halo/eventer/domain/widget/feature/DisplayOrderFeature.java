package com.halo.eventer.domain.widget.feature;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Getter
public class DisplayOrderFeature {

    @Column(name = "display_order")
    private Integer displayOrder;

    private DisplayOrderFeature(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public static DisplayOrderFeature of(Integer displayOrder) {
        return new DisplayOrderFeature(displayOrder);
    }
}
