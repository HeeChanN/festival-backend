package com.halo.eventer.domain.widget.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SquareWidgetProperties implements WidgetProperties {
    private String image;
    private String description;
}
