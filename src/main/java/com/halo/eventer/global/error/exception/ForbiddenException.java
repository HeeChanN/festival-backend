package com.halo.eventer.global.error.exception;

import com.halo.eventer.global.error.ErrorCode;

public class ForbiddenException extends BaseException {
    public ForbiddenException(String message) {
        super(message, ErrorCode.FORBIDDEN);
    }
}
