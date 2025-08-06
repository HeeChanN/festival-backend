package com.halo.eventer.domain.widget.feature;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Getter
public class ImageFeature {

    @Column(name = "image")
    private String image;

    private ImageFeature(String image) {
        this.image = image;
    }

    public static ImageFeature of(String image) {
        return new ImageFeature(image);
    }
}
