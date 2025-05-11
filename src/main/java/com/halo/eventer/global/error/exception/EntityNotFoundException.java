package com.halo.eventer.global.error.exception;

import com.halo.eventer.global.error.ErrorCode;

public class EntityNotFoundException extends BaseException {
    public EntityNotFoundException(String message) {
        super(message, ErrorCode.ENTITY_NOT_FOUND);
    }
}
