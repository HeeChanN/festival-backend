package com.halo.eventer.domain.stamp.dto.stampUser.enums;

public enum Finished {
    ALL,
    TRUE,
    FALSE;

    public Boolean toBoolean() {
        return switch (this) {
            case TRUE -> Boolean.TRUE;
            case FALSE -> Boolean.FALSE;
            case ALL -> null;
        };
    }
}
