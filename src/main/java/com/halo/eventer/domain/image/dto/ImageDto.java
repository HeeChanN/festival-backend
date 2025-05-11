package com.halo.eventer.domain.image.dto;

import com.halo.eventer.domain.image.Image;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ImageDto {
    private Long id;
    private String image;

    @Builder
    private ImageDto(Long id, String image) {
        this.id = id;
        this.image = image;
    }

    public static ImageDto from(Image image) {
        return ImageDto.builder().id(image.getId()).image(image.getImageUrl()).build();
    }
}
