package com.halo.eventer.global.error.exception;

import com.halo.eventer.global.error.ErrorCode;

public class InfraException extends BaseException{
    public InfraException(ErrorCode errorCode) {
        super(errorCode);
    }
}
