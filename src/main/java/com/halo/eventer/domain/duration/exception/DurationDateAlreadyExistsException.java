package com.halo.eventer.domain.duration.exception;

import com.halo.eventer.global.error.ErrorCode;
import com.halo.eventer.global.error.exception.ConflictException;

public class DurationDateAlreadyExistsException extends ConflictException {
    public DurationDateAlreadyExistsException(String message) {
        super(message, ErrorCode.SUB_ADDRESS_ALREADY_EXISTS);
    }
}
