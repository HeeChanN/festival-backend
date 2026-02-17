package com.halo.eventer.domain.widget.properties;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @Type(value = MainWidgetProperties.class, name = "MAIN"),
    @Type(value = UpWidgetProperties.class, name = "UP"),
    @Type(value = MiddleWidgetProperties.class, name = "MIDDLE"),
    @Type(value = SquareWidgetProperties.class, name = "SQUARE"),
    @Type(value = DownWidgetProperties.class, name = "DOWN")
})
public interface WidgetProperties {}
