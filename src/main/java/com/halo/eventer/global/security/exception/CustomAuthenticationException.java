package com.halo.eventer.global.security.exception;

import com.halo.eventer.global.error.ErrorCode;
import com.halo.eventer.global.error.exception.AuthException;

public class CustomAuthenticationException extends AuthException {

    public CustomAuthenticationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
