package com.halo.eventer.domain.map.dto.menu;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MenuUpdateDto {
    private Long id;
    private String name;
    private Integer price;
    private String image;
    private String summary;
}
