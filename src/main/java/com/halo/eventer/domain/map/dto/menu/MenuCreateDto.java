package com.halo.eventer.domain.map.dto.menu;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MenuCreateDto {
    private String name;
    private Integer price;
    private String summary;
    private String image;
}
