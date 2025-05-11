package com.halo.eventer.domain.stamp.exception;

import com.halo.eventer.global.error.exception.ForbiddenException;

public class StampClosedException extends ForbiddenException {
    public StampClosedException(Long id) {
        super(String.format("Stamp with %d is forbidden", id));
    }
}
