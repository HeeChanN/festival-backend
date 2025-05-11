package com.halo.eventer.domain.stamp.exception;

import com.halo.eventer.global.error.exception.EntityNotFoundException;

public class StampUserNotFoundException extends EntityNotFoundException {
    public StampUserNotFoundException(String uuid) {
        super(String.format("StampUser with %s is not found", uuid));
    }

    public StampUserNotFoundException() {
        super("StampUser could not be found");
    }
}
