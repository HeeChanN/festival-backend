package com.halo.eventer.domain.map.dto.mapcategory;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MapCategoryCreateDto {

    @NotNull
    @Size(min = 2, max = 10)
    private String name;

    public MapCategoryCreateDto(String name) {
        this.name = name;
    }
}
