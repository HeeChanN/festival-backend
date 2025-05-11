package com.halo.eventer.domain.festival.exception;

import com.halo.eventer.global.error.ErrorCode;
import com.halo.eventer.global.error.exception.ConflictException;

public class FestivalAlreadyExistsException extends ConflictException {
    public FestivalAlreadyExistsException() {
        super(ErrorCode.SUB_ADDRESS_ALREADY_EXISTS);
    }
}
