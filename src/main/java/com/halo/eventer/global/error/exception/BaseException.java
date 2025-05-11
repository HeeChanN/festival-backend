package com.halo.eventer.global.error.exception;

import com.halo.eventer.global.error.ErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BaseException extends RuntimeException {
    private ErrorCode errorCode;

    public BaseException(String message, final ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public BaseException(final ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
