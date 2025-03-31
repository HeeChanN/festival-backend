package com.halo.eventer.domain.member.exception;

import com.halo.eventer.global.error.ErrorCode;
import com.halo.eventer.global.error.exception.AuthException;

public class LoginFailedException extends AuthException {
    public LoginFailedException() {
        super(ErrorCode.LOGIN_FAILED);
    }
}
