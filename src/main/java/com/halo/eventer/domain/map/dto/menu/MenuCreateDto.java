package com.halo.eventer.domain.map.dto.menu;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MenuCreateDto {

    @NotNull
    @Size(min = 1, max = 20)
    private String name;

    @NotNull
    private Integer price;

    private String summary;

    private String image;

    public MenuCreateDto(String name, Integer price, String summary, String image) {
        this.name = name;
        this.price = price;
        this.summary = summary;
        this.image = image;
    }
}
