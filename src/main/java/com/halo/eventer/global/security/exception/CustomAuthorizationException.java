package com.halo.eventer.global.security.exception;

import com.halo.eventer.global.error.ErrorCode;
import com.halo.eventer.global.error.exception.AuthException;

public class CustomAuthorizationException extends AuthException {

    public CustomAuthorizationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
