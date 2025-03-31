package com.halo.eventer.domain.widget;

public enum WidgetType {
    DEFAULT("default"),
    UP("up"),
    MAIN("main"),
    MIDDLE("middle"),
    SQUARE("square"),
    DOWN("down");

    private final String name;

    WidgetType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
