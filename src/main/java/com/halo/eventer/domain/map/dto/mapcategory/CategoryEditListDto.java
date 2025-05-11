package com.halo.eventer.domain.map.dto.mapcategory;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryEditListDto {
    private List<CategoryEditDto> categoryEditDtos;

    public CategoryEditListDto(List<CategoryEditDto> categoryEditDtos) {
        this.categoryEditDtos = categoryEditDtos;
    }
}
