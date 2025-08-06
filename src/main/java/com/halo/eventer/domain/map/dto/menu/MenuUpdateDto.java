package com.halo.eventer.domain.map.dto.menu;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MenuUpdateDto {

    @NotNull
    @Min(1)
    private Long id;

    @NotNull
    @Size(min = 1, max = 20)
    private String name;

    @NotNull
    private Integer price;

    private String image;
    private String summary;

    public MenuUpdateDto(Long id, String name, Integer price, String image, String summary) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.summary = summary;
    }
}
