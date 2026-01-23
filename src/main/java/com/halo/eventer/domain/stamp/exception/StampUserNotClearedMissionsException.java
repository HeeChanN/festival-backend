package com.halo.eventer.domain.stamp.exception;

import com.halo.eventer.global.error.exception.ForbiddenException;

public class StampUserNotClearedMissionsException extends ForbiddenException {
    public StampUserNotClearedMissionsException(String id) {
        super(String.format("Stamp User %s need to clear missions", id));
    }
}
