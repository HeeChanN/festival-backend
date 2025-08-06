package com.halo.eventer.domain.widget.feature;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Getter
public class DescriptionFeature {

    @Column(name = "description")
    private String description;

    private DescriptionFeature(String description) {
        this.description = description;
    }

    public static DescriptionFeature of(String description) {
        return new DescriptionFeature(description);
    }
}
